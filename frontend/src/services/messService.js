import axios from "axios";

const API = "http://localhost:5000/api/mess";

// Add Mess
export const addMess = async (data) => {
  const form = new FormData();

  Object.entries(data).forEach(([key, value]) => {
    if (key === "images") {
      value.forEach((file) => form.append("images", file));
    } else if (typeof value === "object") {
      form.append(key, JSON.stringify(value));
    } else {
      form.append(key, value);
    }
  });

  const res = await axios.post(`${API}/add`, form, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

// Get All Mess
export const getAllMesses = async () => {
  const res = await axios.get(`${API}/all`);
  return res.data;
};

// Get Messes by Owner
export const getMessesByOwner = async (ownerId) => {
  const res = await axios.get(`${API}/owner/${ownerId}`);
  return res.data;
};

// Get Mess by ID
export const getMessById = async (id) => {
  const res = await axios.get(`${API}/${id}`);
  return res.data;
};

// Update Mess
export const updateMess = async (id, data) => {
  const res = await axios.put(`${API}/${id}`, data);
  return res.data;
};

// Delete Mess
export const deleteMess = async (id) => {
  const res = await axios.delete(`${API}/${id}`);
  return res.data;
};

// Publish Today's Special
export const publishSpecial = async (data) => {
  const res = await axios.post(`${API}/publish-special`, data);
  return res.data;
};

// Add Rating
export const submitMessRating = async (messId, ratingData) => {
  const res = await axios.post(`${API}/${messId}/rate`, ratingData, {
    headers: { "Content-Type": "application/json" },
  });
  return res.data;
};
