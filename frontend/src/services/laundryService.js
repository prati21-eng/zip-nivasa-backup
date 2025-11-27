import axios from "axios";

const API = "http://localhost:5000/api/laundry";

export const addLaundryOwner = async (data) => {
  const res = await axios.post(`${API}/add`, data);
  return res.data;
};

export const getAllLaundryOwners = async () => {
  const res = await axios.get(`${API}/all`);
  return res.data;
};
