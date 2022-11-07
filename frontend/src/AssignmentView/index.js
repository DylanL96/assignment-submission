import React, {useEffect, useRef, useState} from 'react';
import ajax from '../Services/fetchService';
import {useLocalState} from '../util/useLocalStorage';
import StatusBadge from '../StatusBadge';
import {Button, Form, Row, Col, Container, ButtonGroup, DropdownButton, Dropdown} from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import { useUser } from '../UserProvider';
import CommentContainer from '../CommentContainer';

const AssignmentView = () => {
  const navigate = useNavigate();
  const user = useUser();
  const {assignmentId} = useParams();
  const [assignment, setAssignment] = useState({
    // this is for UPDATING the assignment object
    branch: "",
    githubUrl: "",
    number: null,
    status: null,
  });


  const previousAssignmentValue = useRef(assignment);
  const [jwt, setJwt] = useLocalState("", "jwt");  
  const [assignmentEnums, setAssignmentEnums] = useState([]);
  const [assignmentStatuses, setAssignmentStatuses] = useState([]);


  // function is used to update what is being typed into the GitHubURL and Branch text
  // it saves the data to the assignment object
  function updateAssignment(prop, value){
    const newAssignment = {...assignment} // create duplicate of assignment object and put it into newAssignment
    newAssignment[prop] = value; // change newAssignment's values
    setAssignment(newAssignment);
  };

  // saving to DB
  // when you post to url with domain object and then the ID, it means you want to UPDATE that particular assignment  
  function save(status){
    // implies student submitting for the first time
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
        <StatusBadge text={assignment.status}></StatusBadge>
        </Col>
      </Row>
      {assignment ? (
      <> 
        <Form.Group as={Row} className="my-4" controlId="assignmentName">
        <Form.Label column sm="3" md="2">
          Assignment Number:
        </Form.Label>
        <Col sm="9" md="8" lg="6">
          <DropdownButton 
            as={ButtonGroup} 
            id="assignmentName" 
            variant={'info'}
            title={assignment.number ? `Assignment ${assignment.number}` : "Select an Assignment"}
            onSelect={(selectedElement) => {
              // setSelectedAssignment(selectedElement);
              updateAssignment("number", selectedElement)
            }}
          >
            {assignmentEnums.map((assignmentEnum) => (
            <Dropdown.Item 
              key={assignmentEnum.assignmentNum}
              eventKey={assignmentEnum.assignmentNum}>
              {assignmentEnum.assignmentNum}
            </Dropdown.Item>
            ))}
          </DropdownButton>
        </Col>
        </Form.Group>

        <Form.Group as={Row} className="mb-3" controlId="githubUrl">
        <Form.Label column sm="3" md="2">
          Github URL:
        </Form.Label>
        <Col sm="9" md="10" lg="6">
          <Form.Control 
            value={assignment.githubUrl} 
            onChange={(e) => updateAssignment("githubUrl", e.target.value)}
            type="url"
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
            placeholder='example branch name'
          />
        </Col>
        </Form.Group>

        {assignment.status === "Completed" ? (
          <>
            <Form.Group as={Row} className="d-flex align-items-center mb-3" controlId="codeReviewVideoUrl">
              <Form.Label column sm="3" md="2">
                Code Review Video URL:
              </Form.Label>
              <Col sm="9" md="10" lg="6">
                <a style={{fontWeight: "bold"}} href={assignment.codeReviewVideoUrl}>{assignment.codeReviewVideoUrl}</a>
              </Col>
            </Form.Group>
            <div className='d-flex gap-5'>
              <Button variant='secondary' onClick={() => navigate("/dashboard")}>Back</Button>
            </div>
          </>
        ): assignment.status === "Pending Submission" ? (
            <div className='d-flex gap-5'>
            <Button onClick={() => save("Submitted")}>Submit Assignment</Button>
            <Button variant='secondary' onClick={() => navigate("/dashboard")}>Back</Button>
            </div>
            ):          
            <div className='d-flex gap-5'>
            <Button onClick={() => save("Resubmitted")}>Re-Submit Assignment</Button>
            <Button variant='secondary' onClick={() => navigate("/dashboard")}>Back</Button>
            </div>}

            <CommentContainer assignmentId={assignmentId}/>
          </> 
        ):( 
          <></>
      )}
    </Container>
  )
}

export default AssignmentView