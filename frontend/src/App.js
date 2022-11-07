import React, {useEffect, useState} from 'react';
import {Routes, Route} from 'react-router-dom';
import Dashboard from './Dashboard';
import HomePage from './HomePage';
import Login from './Login';
import PrivateRoute from './PrivateRoute';
import AssignmentView from './AssignmentView';
import CodeReviewerDashboard from './CodeReviewerDashboard';
import CodeReviewerAssignmentView from './CodeReviewerAssignmentView';
import jwt_decode from 'jwt-decode';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {useUser} from './UserProvider';

// In this file, anywhere we want to provide the User information, we can do that with the Provider component

const App = () => {
  // const [jwt, setJwt] = useLocalState("", "jwt");
  const [roles, setRoles] = useState([]);
  const user = useUser();

  useEffect(() => {
    setRoles(getRolesFromJWT());
  }, [user.jwt])

  function getRolesFromJWT(){
    // get role from jwt and assign via setRole()
    if(user.jwt){
      const decodedJwt = jwt_decode(user.jwt);
      return decodedJwt.authorities;
    }
    // if dont have jwt, then just return empty array and dont try to decode null
    return [];
  }

  return(
      <Routes>
        <Route path="/dashboard" element={
          roles.find((role) => role === "ROLE_CODE_REVIEWER") ? (
            <PrivateRoute>
              <CodeReviewerDashboard/>
            </PrivateRoute> 
          ) : (
            <PrivateRoute>
              <Dashboard/>
            </PrivateRoute>
          )}/>
        <Route path="/assignments/:assignmentId" element={
          roles.find((role) => role === "ROLE_CODE_REVIEWER") ? (
          <PrivateRoute>
            <CodeReviewerAssignmentView/> 
          </PrivateRoute>
        ) : (
          <PrivateRoute>
          <AssignmentView/> 
        </PrivateRoute>
        )}/>
        <Route path="login" element={<Login/>}/>
        <Route path="/" element={<HomePage/>}/>
      </Routes>
  )
}

export default App;
