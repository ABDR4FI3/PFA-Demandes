import Nav from "./Components/Nav";
import Reclamation from "./Components/Reclamation";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
function AdminHome() {
  const [reclamations, setReclamations] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const checkTokenValidity = async (token) => {
      try {
        const response = await fetch(
          `http://localhost:9090/checkToken?token=${token}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const data = await response.json();
        console.log("Token validity response:", data);

        return data; // This will be a boolean value
      } catch (error) {
        console.error("There was a problem checking token validity:", error);
        return false;
      }
    };

    const token = localStorage.getItem("token");
    if (token) {
      checkTokenValidity(token).then((valid) => {
        console.log(valid);
        if (valid) {
          // Token is valid, navigate to home
          //navigate("/home");
        } else {
          // Token is not valid, remove it and navigate to login
          localStorage.removeItem("token");
          navigate("/Login");
        }
      });
    } else {
      // Token not found in localStorage, navigate to login
      navigate("/Login");
    }
  }, []);

  //
  useEffect(() => {
    fetchDataFromApi();
  }, []);
  //Function to Fetch Data from My API
  const fetchDataFromApi = async () => {
    const token = localStorage.getItem("token");
    console.log(token);
    const response = await fetch("http://localhost:9090/demandes", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: token,
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || "Failed to fetch data");
    }
    console.log(data);
    setReclamations(data);
    return data;
  };
  //function to Reject Reclamation
  const rejectReclamation = async (id) => {
    console.log("test");
    try {
      const token = localStorage.getItem("token");
      console.log("id ===> " + id);
      const response = await fetch(
        `http://localhost:9090/reclamation/reject?id=${id}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: token, // Adjust as per your needs
        }
      );
      console.log(token);
      const data = await response.json();
      console.log("Response:", data);
      // Perform any other actions based on the response, such as updating UI
    } catch (error) {
      console.error("Error:", error);
      // Handle error scenarios
    }
  };
  //function to Solve Reclamation
  const solveReclamation = async (id) => {
    console.log("test");
    try {
      const token = localStorage.getItem("token");
      console.log("id ===> " + id);
      const response = await fetch(
        `http://localhost:9090/reclamation/solve?id=${id}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: token, // Adjust as per your needs
        }
      );
      console.log(token);
      const data = await response.json();
      console.log("Response:", data);
      // Perform any other actions based on the response, such as updating UI
    } catch (error) {
      console.error("Error:", error);
      // Handle error scenarios
    }
  };

  //__________________________________________________________
  return (
    <>
      <Nav></Nav>
      <div className="mt-16 flex justify-center items-center w-screen h-full">
        <div className="rounded-xl mydiv grid grid-cols-4 gap-5 p-4 border-solid border-2 border-blue-900 mt-2 ml-2">
          {reclamations.map((reclamation) => (
            <Reclamation
              key={reclamation.idDemande}
              id={reclamation.idDemande}
              title={reclamation.title}
              date={reclamation.date}
              details={reclamation.sujet}
              status={reclamation.etat}
            >
              <div className="grid grid-cols-2 gap-5 w-full">
                <button
                  className="ring-1 bg-cyan-200 rounded-2xl self-end hover:ring-white"
                  onClick={() => solveReclamation(reclamation.idDemande)}
                >
                  Solve!
                </button>

                <button
                  className="ring-1 bg-red-400 rounded-2xl self-end hover:ring-white"
                  onClick={() => rejectReclamation(reclamation.idDemande)}
                >
                  Reject!
                </button>
              </div>
            </Reclamation>
          ))}
        </div>
      </div>
    </>
  );
}
export default AdminHome;
