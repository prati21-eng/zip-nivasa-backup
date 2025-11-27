// frontend/src/services/socket.js

// FIX: SockJS needs global in browser
window.global = window;

import SockJS from "sockjs-client/dist/sockjs";
import Stomp from "stompjs";

let stompClient = null;
let onMessageCallback = null;
let onTypingCallback = null;
let onStopTypingCallback = null;
let isConnecting = false;

export function connectSocket(userId, onMessage, onTyping, onStopTyping) {
  if (!userId) return;

  // Avoid multiple connections (StrictMode)
  if (stompClient && stompClient.connected) {
    console.log("STOMP already connected");
    onMessageCallback = onMessage;
    onTypingCallback = onTyping;
    onStopTypingCallback = onStopTyping;
    return;
  }
  if (isConnecting) {
    console.log("STOMP: connection in progress, skipping");
    return;
  }

  onMessageCallback = onMessage;
  onTypingCallback = onTyping;
  onStopTypingCallback = onStopTyping;

  const sock = new SockJS("http://localhost:5000/ws");
  stompClient = Stomp.over(sock);
  stompClient.debug = () => {}; // Disable logs

  isConnecting = true;

  stompClient.connect(
    {},
    () => {
      console.log("Connected to STOMP");
      isConnecting = false;

      try {
        // Subscribe to messages
        stompClient.subscribe(`/user/${userId}/queue/messages`, (msg) => {
          if (onMessageCallback) onMessageCallback(JSON.parse(msg.body));
        });

        // Subscribe to typing events
        stompClient.subscribe(`/user/${userId}/queue/typing`, (msg) => {
          if (onTypingCallback) onTypingCallback(JSON.parse(msg.body).sender);
        });

        stompClient.subscribe(`/user/${userId}/queue/stop-typing`, (msg) => {
          if (onStopTypingCallback)
            onStopTypingCallback(JSON.parse(msg.body).sender);
        });

        // Register user to backend
        stompClient.send(
          "/app/chat.register",
          {},
          JSON.stringify({ userId })
        );
      } catch (err) {
        console.error("STOMP subscribe/register failed:", err);
      }
    },
    (error) => {
      console.error("STOMP connection error:", error);
      isConnecting = false;
    }
  );
}

export function sendChatMessage(sender, receiver, message) {
  if (!stompClient || !stompClient.connected) return;

  stompClient.send(
    "/app/chat.send",
    {},
    JSON.stringify({ sender, receiver, message })
  );
}

export function sendTyping(sender, receiver) {
  if (!stompClient || !stompClient.connected) return;

  stompClient.send(
    "/app/chat.typing",
    {},
    JSON.stringify({ sender, receiver })
  );
}

export function stopTyping(sender, receiver) {
  if (!stompClient || !stompClient.connected) return;

  stompClient.send(
    "/app/chat.stopTyping",
    {},
    JSON.stringify({ sender, receiver })
  );
}

export function disconnectSocket() {
  if (stompClient) {
    stompClient.disconnect();
    stompClient = null;
  }
}
