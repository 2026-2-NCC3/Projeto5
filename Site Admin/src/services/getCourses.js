import api from "./httpclient";

export async function getCourses() {
  try {
    const response = await api.get('/courses');
    return response.data;
  } catch (error) {
    console.error(error);
    return [];
  }
}