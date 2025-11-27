import axios from "axios";

const API = "http://localhost:5000/api/chat";

export const sendMessageREST = async (data) => {
  const token = localStorage.getItem("token");

  const res = await axios.post(`${API}/send`, data, {
    headers: { Authorization: `Bearer ${token}` },
  });

  return res.data;
};

export const getHistory = async (receiverId) => {
  const token = localStorage.getItem("token");

  const res = await axios.get(`${API}/history/${receiverId}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  return res.data;
};

export const getConversations = async () => {
  const token = localStorage.getItem("token");

  const res = await axios.get(`${API}/conversations`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  return res.data;
};

export const markAsRead = async (partnerId) => {
  const token = localStorage.getItem("token");

  const res = await axios.post(`${API}/mark-read`, { partnerId }, {
    headers: { Authorization: `Bearer ${token}` },
  });

  return res.data;
};
