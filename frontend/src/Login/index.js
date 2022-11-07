import React, {useEffect, useState} from 'react';
import {Container, Row, Col, Button, Form} from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useUser } from '../UserProvider';

const Login = () => {
  const user = useUser();
  const navigate = useNavigate();
  // const [jwt, setJwt] = useLocalState("", "jwt"); 
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    if(user.jwt) navigate("/dashboard")
  }, [user])

  function sendLoginRequest(){
    const reqBody = {
      username : username,
      password : password
    };

    fetch("api/auth/login", {
      headers: {
        "Content-Type" : "application/json"
      },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
    .then((response) => {
      if(response.status === 200){
        return Promise.all([response.json(), response.headers]);
      } else {
        return Promise.reject("invalid login attempt")
      }
    })
    .then(([body, headers]) => {
      user.setJwt(headers.get("authorization"));
      //  updateUser(headers.get("authorization"))
      // setJwt(headers.get("authorization"));
      // navigate("/dashboard") error with the JWT
      // window.location.href = "dashboard";
    })
    .catch((message) => {
      alert(message)
    })
  
  }
  return (
    // className='justify-content-center' this will center the stuff
    <>
    <Container className='mt-5'>
      <Row className='justify-content-center'>
        <Col md='8' lg='6'>
          <Form.Group className='mb-3' controlId='username'>
            <Form.Label className='fs-4'>Username</Form.Label>
            <Form.Control type="email" 
                          placeholder='joe@gmail.com' 
                          value={username} 
                          onChange={(e) => setUsername(e.target.value)}/>
          </Form.Group>
        </Col>
      </Row>
      <Row className='justify-content-center'>
        <Col md='8' lg='6'> 
          <Form.Group className='mb-3' controlId='password'>
            <Form.Label className='fs-4'>Password</Form.Label>
            <Form.Control type="password" 
                          placeholder='enter password' 
                          value={password} 
                          onChange={(e) => setPassword(e.target.value)}/>
          </Form.Group>
        </Col>
      </Row>
      <Row className='justify-content-center'>
        <Col md='8' lg='6' className='mt-2 d-flex flex-column gap-5 flex-md-row justify-content-between'>
            <Button id="submit" 
                    type='button' 
                    onClick={() => sendLoginRequest()}>Login</Button>
            <Button id="submit" 
                    variant='secondary' 
                    type='button' 
                    onClick={() => navigate('/')}>Exit</Button>
        </Col>
      </Row>
    </Container>
    </>

  )
}

export default Login