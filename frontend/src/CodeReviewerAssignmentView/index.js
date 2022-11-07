import React, {useEffect, useRef, useState} from 'react';
import ajax from '../Services/fetchService';
import {useLocalState} from '../util/useLocalStorage';
import {Button, Form, Row, Col, Container, Badge, ButtonGroup, DropdownButton, Dropdown} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import CommentContainer from '../CommentContainer';

const CodeReviewerAssignmentView = () => {
  let navigate = useNavigate();
  const assignmentId = window.location.href.split("/assignments/")[1];
  const [assignment, setAssignment] = useState({
    // this is for UPDATING the assignment object
    branch: "",
    githubUrl: "",
    number: null,
    status: null,
  });

  const [jwt, setJwt] = useLocalState("", "jwt");  
  const [assignmentEnums, setAssignmentEnums] = useState([]);
  const [assignmentStatuses, setAssignmentStatuses] = useState([]);
  // const [selectedAssignment, setSelectedAssignment] = useState(null);

  const previousAssignmentValue = useRef(assignment);

  // function is used to update what is being typed into the GitHubURL and Branch text
  // it saves the data to the assignment object
  function updateAssignment(prop, value){
    const newAssignment = {...assignment} // create duplicate of assignment object and put it into newAssignment
    newAssignment[prop] = value; // change newAssignment's values
    setAssignment(newAssignment);
  }

  // saving to DB
  // this talks to the AssignmentStatusEnum java class
  function save(status){
    if(status && assignment.status !== status){
      updateAssignment("status", status);
    } else {
      persist();
    }

  }

  useEffect(() => {
    if(previousAssignmentValue.current.status !== assignment.status){
      persist()
    }
    previousAssignmentValue.current = assignment;
  }, [assignment]);

  function persist() {
    ajax(`/api/assignments/${assignmentId}`, "PUT", jwt, assignment).then(
      (assignmentData) => {
      setAssignment(assignmentData); 
    });
  }

  // GET response to the specific assignment ID 
  useEffect(() => {
    ajax(`/api/assignments/${assignmentId}`, "GET", jwt).then(
      assignmentResponse => {
        let assignmentData = assignmentResponse.assignment;
        if(assignmentData.branch === null){
          assignmentData.branch = "";
        }
        if(assignmentData.githubUrl === null){
          assignmentData.githubUrl = "";
        }
      setAssignment(assignmentData);
      setAssignmentEnums(assignmentResponse.assignmentEnums); // gives us access to the assignment enums from backend
      setAssignmentStatuses(assignmentResponse.statusEnums);
    })
  }, []);

  return (
    <Container className='mt-5'>
      <Row className='d-flex align-items-center'>
        <Col>
          {assignment.number ? <h1>Assignment {assignment.number}</h1> : <></>}
        </Col>
        <Col>
          <Badge pill bg="info" style={{fontSize: "1em"}}>{assignment.status}</Badge>
        </Col>
      </Row>
      {assignment ? (
      <> 
        <Form.Group as={Row} className="mb-3" controlId="githubUrl">
        <Form.Label column sm="3" md="2">
          Github URL:
        </Form.Label>
        <Col sm="9" md="10" lg="6">
          <Form.Control 
            value={assignment.githubUrl} 
            onChange={(e) => updateAssignment("githubUrl", e.target.value)}
            type="url"
            readOnly
            placeholder='https://github.com/username/repo-name'
          />
        </Col>
        </Form.Group>

        <Form.Group as={Row} className="mb-3" controlId="branch">
        <Form.Label column sm="3" md="2">
          Branch:
        </Form.Label>
        <Col sm="9" md="10" lg="6">
          <Form.Control 
            value={assignment.branch} 
            onChange={(e) => updateAssignment("branch", e.target.value)}
            type="url"
            readOnly
            placeholder='example branch name'
          />
        </Col>
        </Form.Group>

        <Form.Group as={Row} className="mb-3" controlId="githubUrl">
        <Form.Label column sm="3" md="2">
          Video Review URL:
        </Form.Label>
        <Col sm="9" md="10" lg="6">
          <Form.Control 
            value={assignment.codeReviewVideoUrl} 
            onChange={(e) => updateAssignment("codeReviewVideoUrl", e.target.value)}
            type="url"
            placeholder='https://screencast-o-matic.com/something'
          />
        </Col>
        </Form.Group>

        <div className='d-flex gap-5'>
          {assignment.status === "Completed" ? (
          <Button variant='secondary'
          onClick={() => save(assignmentStatuses[2].status)}>Re-Claim</Button> 
          ):( 
          <Button onClick={() => save(assignmentStatuses[4].status)}>Complete Review</Button>)}
          

          {assignment.status === "Needs Update" ? (
            <Button size='lg' variant="secondary" onClick={() => save(assignmentStatuses[2].status)}>
            Re-Claim</Button>
            ):(
            <Button size='lg' variant="danger" onClick={() => save(assignmentStatuses[3].status)}>
              Reject Assignment</Button>
          )}

          <Button variant='secondary' onClick={() => navigate("/dashboard")}>Back</Button>
        </div>
        
        <CommentContainer assignmentId={assignmentId}/>
        </> 
      ):( 
      <></>
      )}
    </Container>
  )
}

export default CodeReviewerAssignmentView