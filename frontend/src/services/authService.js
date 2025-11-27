import axios from "axios";

const API = "http://localhost:5000/api/auth";

export const registerUser = async (data) => {
  const res = await axios.post(`${API}/register`, data);
  return res.data;
};

export const loginUser = async (data) => {
  const res = await axios.post(`${API}/login`, data);
  return res.data;
};

export const getMe = async () => {
  const token = localStorage.getItem("token");
  const res = await axios.get(`${API}/me`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data.data; // ✔ now returns only the user object
};
