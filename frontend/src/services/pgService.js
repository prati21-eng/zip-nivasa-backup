import axios from "axios";

const API = "http://localhost:5000/api/pgs";

// Get all PGs (public)
export const getAllPGs = async () => {
  const res = await axios.get(API);
  return res.data;
};

// Get PG by ID (public)
export const getPGById = async (id) => {
  const res = await axios.get(`${API}/${id}`);
  return res.data;
};

// Get PGs of logged-in owner
export const getPGsByOwner = async () => {
  const token = localStorage.getItem("token");

  const res = await axios.get(`${API}/owner/list`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  return res.data;
};

// Create PG (owner only)
export const createPG = async (data) => {
  const token = localStorage.getItem("token");

  const form = new FormData();
  Object.entries(data).forEach(([key, value]) => {
    if (key === "images") {
      value.forEach((file) => form.append("images", file));
    } else {
      form.append(key, value);
    }
  });

  const res = await axios.post(API, form, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "multipart/form-data",
    },
  });

  return res.data;
};
