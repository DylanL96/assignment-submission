import React, { useEffect, useState } from 'react';
// import {useLocalState} from '../util/useLocalStorage';
import {useNavigate} from 'react-router-dom';
import ajax from '../Services/fetchService';
import {Card, Button, Row, Col} from 'react-bootstrap'
import StatusBadge from '../StatusBadge';
import {useUser} from '../UserProvider';

const Dashboard = () => {
  const navigate = useNavigate();
  // const [jwt, setJwt] = useLocalState("", "jwt"); 
  const user = useUser();
  const [assignments, setAssignments] = useState(null);

  // This useEffect is to GET the assignment data
  useEffect(() => {
    ajax("api/assignments", "GET", user.jwt).then(assignmentsData => {
      setAssignments(assignmentsData)
    });
    if(!user.jwt) navigate("/login")
  }, [user.jwt]);

  // this will communicate to the AssignmentController java class
  const createAssignment = () => {
    ajax("api/assignments", "POST", user.jwt).then(assignment => {
      window.location.href = `/assignments/${assignment.id}`;
      // navigate(`/assignments/${assignment.id}`);
    })
  }

  return (
    <div style={{margin: '2em'}}>
      <Row>
        <Col>
          <div className='d-flex justify-content-end' style={{cursor: 'pointer'}} href='#' onClick={() => {
            // setJwt(null);
            user.setJwt(null)
            // navigate('/login');
          }}>Logout</div>
        </Col>
      </Row>
      <div className='mb-5'>
       <Button size='lg' onClick={() => createAssignment()}>Submit New Assignment</Button>
      </div>
      {assignments ? (
        <div className='d-grid' style={{gridTemplateColumns: "repeat(auto-fit, 18rem)"}}>
        {assignments.map(assignment => (
          <Col key={assignment.id}>
            <Card key={assignment.id} style={{ width: '18rem', height: '18rem' }}>
              <Card.Body className='d-flex flex-column justify-content-around'>
                <Card.Title>Assignment #{assignment.number}</Card.Title>
                <div className='d-flex align-items-start'>
                  <StatusBadge text={assignment.status}></StatusBadge>
                </div>
                
                <Card.Text style={{marginTop: "1em"}}>
                  <p>
                  <b>Github URL:</b>{assignment.githubUrl}
                  </p>
                  <p>
                  <b>Branch:</b>{assignment.branch}
                  </p>
                </Card.Text>
                <Button variant='secondary' onClick={() => {
                  window.location.href = `/assignments/${assignment.id}`}}>Edit</Button>
              </Card.Body>
            </Card>          
          </Col>
        ))}
        </div>
      ): (
        <></>
      )}

    </div>
  );
}

export default Dashboard