import React, { useEffect, useState } from "react";
import { Button, Card, Col, Container, Row } from "react-bootstrap";
import jwt_decode from "jwt-decode";
import ajax from "../Services/fetchService";
import StatusBadge from "../StatusBadge";
import { useNavigate } from "react-router-dom";
import { useUser } from "../UserProvider";
import moment from "moment";

const CodeReviewerDashboard = () => {
  const navigate = useNavigate();
  const user = useUser();
  const [assignments, setAssignments] = useState(null);

  useEffect(() => {
    if (!user.jwt) navigate("/login");
  });
  function editReview(assignment) {
    navigate(`/assignments/${assignment.id}`);
    // window.location.href = `/assignments/${assignment.id}`;
  }

  function claimAssignment(assignment) {
    const decodedJwt = jwt_decode(user.jwt);
    const codeReviewer = {
      username: decodedJwt.sub,
    };

    assignment.codeReviewer = codeReviewer;
    assignment.status = "In Review";
    ajax(`/api/assignments/${assignment.id}`, "PUT", user.jwt, assignment).then(
      (updatedAssignment) => {
        const assignmentsCopy = [...assignments];
        const i = assignmentsCopy.findIndex((a) => a.id === assignment.id);
        assignmentsCopy[i] = updatedAssignment;
        setAssignments(assignmentsCopy);
      }
    );
  }

  useEffect(() => {
    ajax("api/assignments", "GET", user.jwt).then((assignmentsData) => {
      setAssignments(assignmentsData);
      console.log(assignmentsData);
    });
  }, [user.jwt]);

  return (
    <>
      <Container>
        <Row>
          <Col>
            <div className="h1">Code Reviewer Dashboard</div>
          </Col>
        </Row>
        <Row>
         <Col>
           <div className='d-flex justify-content-end' style={{cursor: 'pointer'}} href='#' onClick={() => {
             user.setJwt(null);
             navigate('/login');
           }}>Logout</div>
         </Col>
         </Row>
        <div className="assignment-wrapper in-review">
          <div className="assignment-wrapper-title h3 px-2">In Review</div>
          {assignments &&
          assignments.filter((assignment) => assignment.status === "In Review")
            .length > 0 ? (
            <div
              className="d-grid gap-5"
              style={{ gridTemplateColumns: "repeat(auto-fit, 18rem)" }}
            >
              {assignments
                .filter((assignment) => assignment.status === "In Review")
                .map((assignment) => (
                  <Card key={assignment.id} style={{ width: "18rem" }}>
                    <Card.Body className="d-flex flex-column justify-content-around">
                      <Card.Title>Assignment #{assignment.number}</Card.Title>
                      {assignment.name ? (
                        <Card.Subtitle
                          style={{ marginBottom: "0.5em" }}
                          className="text-muted"
                        >
                          {assignment.name}
                        </Card.Subtitle>
                      ) : (
                        <></>
                      )}
                      <div className="d-flex align-items-start">
                        <StatusBadge text={assignment.status} />
                      </div>
                      <Card.Text style={{ marginTop: "1em" }}>
                        <p>
                          <b>GitHub URL</b>: {assignment.githubUrl}
                        </p>
                        <p>
                          <b>Branch</b>: {assignment.branch}
                        </p>
                        <p>
                          <b>Student</b>: {assignment.user.name}
                        </p>
                      </Card.Text>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          editReview(assignment);
                        }}
                      >
                        Edit
                      </Button>
                    </Card.Body>
                  </Card>
                ))}
            </div>
          ) : (
            <div>No assignments found</div>
          )}
        </div>

        <div className="assignment-wrapper submitted">
          <div className="assignment-wrapper-title h3 px-2">
            Awaiting Review
          </div>
          {assignments &&
          assignments.filter(
            (assignment) =>
              assignment.status === "Submitted" ||
              assignment.status === "Resubmitted"
          ).length > 0 ? (
            <div
              className="d-grid gap-5"
              style={{ gridTemplateColumns: "repeat(auto-fit, 18rem)" }}
            >
              {assignments
                .filter(
                  (assignment) =>
                    assignment.status === "Submitted" ||
                    assignment.status === "Resubmitted"
                )
                .sort((a, b) => {
                  if (a.status === "Resubmitted") return -1;
                  if (a.submittedDate && b.submittedDate) {
                    return (
                      new Date(a.submittedDate) - new Date(b.submittedDate)
                    );
                  } else return 1;
                })
                .map((assignment) => (
                  <Card key={assignment.id} style={{ width: "18rem" }}>
                    <Card.Body className="d-flex flex-column justify-content-around">
                      <Card.Title>Assignment #{assignment.number}</Card.Title>

                      {assignment.name ? (
                        <Card.Subtitle
                          style={{ marginBottom: "0.5em" }}
                          className="text-muted"
                        >
                          {assignment.name}
                        </Card.Subtitle>
                      ) : (
                        <></>
                      )}
                      <div className="d-flex align-items-start">
                        <StatusBadge text={assignment.status} />
                      </div>

                      <Card.Text style={{ marginTop: "1em" }}>
                        <p>
                          <b>GitHub URL</b>: {assignment.githubUrl}
                        </p>
                        <p>
                          <b>Branch</b>: {assignment.branch}
                        </p>
                        <p>
                          <b>Student</b>: {assignment.user.name}
                        </p>
                        <p>
                          <b>Submitted</b>:{" "}
                          {assignment.submittedDate
                            ? moment(assignment.submittedDate).format(
                                "MMMM Do YYYY"
                              )
                            : "No submitted date"}
                        </p>
                      </Card.Text>

                      <Button
                        variant="secondary"
                        onClick={() => {
                          claimAssignment(assignment);
                        }}
                      >
                        Claim
                      </Button>
                    </Card.Body>
                  </Card>
                ))}
            </div>
          ) : (
            <div>No assignments found</div>
          )}
        </div>

        <div className="assignment-wrapper needs-update">
          <div className="assignment-wrapper-title h3 px-2">Needs Update</div>
          {assignments &&
          assignments.filter(
            (assignment) => assignment.status === "Needs Update"
          ).length > 0 ? (
            <div
              className="d-grid gap-5"
              style={{ gridTemplateColumns: "repeat(auto-fit, 18rem)" }}
            >
              {assignments
                .filter((assignment) => assignment.status === "Needs Update")
                .map((assignment) => (
                  <Card key={assignment.id} style={{ width: "18rem" }}>
                    <Card.Body className="d-flex flex-column justify-content-around">
                      <Card.Title>Assignment #{assignment.number}</Card.Title>
                      {assignment.name ? (
                        <Card.Subtitle
                          style={{ marginBottom: "0.5em" }}
                          className="text-muted"
                        >
                          {assignment.name}
                        </Card.Subtitle>
                      ) : (
                        <></>
                      )}
                      <div className="d-flex align-items-start">
                        <StatusBadge text={assignment.status} />
                      </div>

                      <Card.Text style={{ marginTop: "1em" }}>
                        <p>
                          <b>GitHub URL</b>: {assignment.githubUrl}
                        </p>
                        <p>
                          <b>Branch</b>: {assignment.branch}
                        </p>
                        <p>
                          <b>Student</b>: {assignment.user.name}
                        </p>
                      </Card.Text>

                      <Button
                        variant="secondary"
                        onClick={() => {
                          navigate(`/assignments/${assignment.id}`);
                        }}
                      >
                        View
                      </Button>
                    </Card.Body>
                  </Card>
                ))}
            </div>
          ) : (
            <div>No assignments found</div>
          )}
        </div>
      </Container>
    </>
  );
};

export default CodeReviewerDashboard;
// import React, { useEffect, useState } from 'react';
// import ajax from '../Services/fetchService';
// import {Card, Button, Row, Col, Container} from 'react-bootstrap';
// import StatusBadge from '../StatusBadge';
// import jwt_decode from 'jwt-decode';
// import { useNavigate } from 'react-router-dom';
// import { useUser } from '../UserProvider';

// const CodeReviewerDashboard = () => {
//   const navigate = useNavigate();
//   const user = useUser();
//   // const [jwt, setJwt] = useLocalState("", "jwt"); 
//   const [assignments, setAssignments] = useState(null);

//   useEffect(() => {
//     if(!user.jwt) navigate("/login")
//   });

//   function editReview (assignment){
//     // navigate(`/assignments/${assignment.id}`);
//     window.location.href = `/assignments/${assignment.id}`;
//   }

//   // assign current logged in code reviewer their ID, into this assignment
//   function claimAssignment(assignment){
//     const decodedJwt = jwt_decode(user.jwt);
//     const codeReviewer = {
//       username: decodedJwt.sub,
//     };

//     assignment.codeReviewer = user;
//     assignment.status = "In Review";
//     ajax(`/api/assignments/${assignment.id}`, "PUT", user.jwt, assignment).then(updatedAssignment => {
//       //TODO: update the view for the assignment that changed
//       // copy current assignments list, then modify the copy and then set the assignments back
//       const assignmentsCopy = [...assignments]; // this will create a copy of the assignments array into a new one
//       const i = assignmentsCopy.findIndex(a => a.id === assignment.id); // Find index of assignment that matches the id
//       assignmentsCopy[i] = updatedAssignment; // take assignmentCopy and replace at index i and replace with the one from the backend
//       setAssignments(assignmentsCopy); // set the entire assignment back to the assignments copy
//       // this will update the view
//     })
//   }

//   // This useEffect is to GET the assignment data
//   useEffect(() => {
//     ajax("api/assignments", "GET", user.jwt).then(assignmentsData => {
//       setAssignments(assignmentsData)
//     });
//   }, [user.jwt]);

//   return (
//     <Container>
//       <Row>
//         <Col>
//           <div className='d-flex justify-content-end' style={{cursor: 'pointer'}} href='#' onClick={() => {
//             user.setJwt(null);
//             navigate('/login');
//           }}>Logout</div>
//         </Col>
//       </Row>
//       <Row>
//         <Col>
//           <div className='h1'>Code Reviewer Dashboard</div>
//         </Col>
//       </Row>
//       <div className='assignment-wrapper in-review'>
//         <div className='assignment-wrapper-title h3 px-2'>In Review</div>
//           {assignments && assignments.filter(assignment => assignment.status === "In Review").length > 0 ? (   
//             <div className='d-grid' style={{gridTemplateColumns: "repeat(auto-fit, 18rem)"}}>
//               {assignments.filter(assignment => assignment.status === "In Review").map(assignment => (
//                 <Col key={assignment.id}>
//                   <Card key={assignment.id} style={{ width: '18rem', height: '18rem' }}>
//                     <Card.Body className='d-flex flex-column justify-content-around'>
//                       <Card.Title>Assignment #{assignment.number}</Card.Title>
//                       <div className='d-flex align-items-start'>
//                         <StatusBadge text={assignment.status}></StatusBadge>
//                       </div>
                      
//                       <Card.Text style={{marginTop: "1em"}}>
//                         <b>Github URL:</b>{assignment.githubUrl}
//                         <b>Branch:</b> {assignment.branch}
//                       </Card.Text>
//                       <Button variant='secondary' onClick={() => {editReview(assignment)}}>Edit</Button>
//                     </Card.Body>
//                   </Card>          
//                 </Col>
//               ))}
//             </div>
//             ): (
//             <div>No assignments found</div>
//           )}
//       </div>

//       <div className='assignment-wrapper submitted'>
//         <div className='assignment-wrapper-title h3 px-2'>Awaiting Review</div>          
//           {assignments && assignments.filter(assignment => assignment.status === "Submitted" || assignment.status === "Resubmitted").length > 0 ? (
//             <div className='d-grid' style={{gridTemplateColumns: "repeat(auto-fit, 18rem)"}}>
//               {assignments.filter(assignment => assignment.status === "Submitted" || assignment.status === "Resubmitted").sort((a,b)=> {
//                 if(a.status === "Resubmitted")
//                   return -1; // sort resubmitted status first
//                 else
//                   return 1;
//               }).map(assignment => (
//                 <Col key={assignment.id}>
//                   <Card key={assignment.id} style={{ width: '18rem', height: '18rem' }}>
//                     <Card.Body className='d-flex flex-column justify-content-around'>
//                       <Card.Title>Assignment #{assignment.number}</Card.Title>
//                       <div className='d-flex align-items-start'>
//                         <StatusBadge text={assignment.status}></StatusBadge>
//                       </div>
                      
//                       <Card.Text style={{marginTop: "1em"}}>
//                         <b>Github URL:</b>{assignment.githubUrl}
//                         <b>Branch:</b> {assignment.branch}
//                       </Card.Text>
//                       <Button variant='secondary' onClick={() => {claimAssignment(assignment)}}>Claim</Button>
//                     </Card.Body>
//                   </Card>          
//                 </Col>
//               ))}
//             </div>
//           ) : (
//             <div>No assignments found</div>
//           )}
//       </div>

//       <div className='assignment-wrapper needs-update'>
//         <div className='assignment-wrapper-title h3 px-2'>Needs Update</div>          
//             {assignments && assignments.filter(assignment => assignment.status === "Needs Update").length > 0 ? (
//               <div className='d-grid' style={{gridTemplateColumns: "repeat(auto-fit, 18rem)"}}>
//                 {assignments.filter(assignment => assignment.status === "Needs Update").map(assignment => (
//                   <Col key={assignment.id}>
//                     <Card key={assignment.id} style={{ width: '18rem', height: '18rem' }}>
//                       <Card.Body className='d-flex flex-column justify-content-around'>
//                         <Card.Title>Assignment #{assignment.number}</Card.Title>
//                         <div className='d-flex align-items-start'>
//                           <StatusBadge text={assignment.status}></StatusBadge>
//                         </div>
                        
//                         <Card.Text style={{marginTop: "1em"}}>
//                           <b>Github URL:</b>{assignment.githubUrl}
//                           <b>Branch:</b> {assignment.branch}
//                         </Card.Text>
//                         <Button variant='secondary' onClick={() => {window.location.href=`/assignments/${assignment.id}`}}>View</Button>
//                       </Card.Body>
//                     </Card>          
//                   </Col>
//                 ))}
//               </div>
//             ) : (
//               <div>No assignments found</div>
//             )}
//       </div>

//     </Container>
//   );
// }

// export default CodeReviewerDashboard