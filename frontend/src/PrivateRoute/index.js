import React, { useState } from 'react'
import { Navigate } from 'react-router-dom';
import ajax from '../Services/fetchService';
// import { useLocalState } from '../util/useLocalStorage';
import { useUser } from '../UserProvider';

// render the children if we are authenticated 
// if not authenticated, redirect to login page
// pass in the children, which would be any component wrapped between the <PrivateRoute> tags in App.js
const PrivateRoute = ({children}) => {
  // const [jwt, setJwt] = useLocalState("", "jwt"); // dont use localState anymore, because it can be used in Provider
  const user = useUser();
  const [isLoading, setIsLoading] = useState(true);
  const [isValid, setIsValid] = useState(null);

  if(user){
    ajax(`/api/auth/validate?token=${user.jwt}`, 'GET', user.jwt).then(isValid => {
      setIsValid(isValid);
      setIsLoading(false);
    })
  } else {
    return <Navigate to="/login"/>;
  }

  // if we're loading, WAIT, do not navigate from screen, and then when we are ready with isValid, we check if it is true
  // if true, we render the children, otherwise we navigate to login
  return isLoading ? <div>Loading...</div> : isValid === true ? children : <Navigate to="/login"/>;
}

export default PrivateRoute;