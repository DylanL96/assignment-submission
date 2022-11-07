import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { UserProvider } from './UserProvider';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <UserProvider>
        <App />
      </UserProvider>
    </BrowserRouter>
  </React.StrictMode>
);

// React Context allows the ability to store state in one place and pass it to all components in a given tree of components
// This is know n as a global variable
// React Context should be used sparingly, as it is most useful when MANY of the components in a given treen need access to a variable
// If you need to access the variable from ONE of the components in a tree, then you should favor composition