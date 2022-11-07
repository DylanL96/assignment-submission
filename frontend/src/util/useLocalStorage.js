
// pass in defaultValue and key
// key is used to identify which of the storage items we want to grab or store
// localstorage can store more than 1 key-value pairs and persist them for long term use
// local storage allows us to store data in our browser for that particular website we are on
// allows us to persist data across views, different pages etc

import { useEffect, useState } from "react";

// when using localstorage, everything is stored as a string
const useLocalState = (defaultValue, key) => {

  const [value, setValue] = useState(() => {
    const localStorageValue = localStorage.getItem(key); // get item based on the key we are passing
    // in this case, we are storing JWT, so the JWT key will be the string
    // initially, the JWT key will NOT exist in localstorage, so it will return undefined

    // if localStorageValue is NOT null, parse it, otherwise return defaultValue
    return localStorageValue !== null ? JSON.parse(localStorageValue) : defaultValue;
    // Remember, value in localstorage is a string. But we want to work with objects, so we have to parse the string and parse it back to an object.
  });

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value))
  }, [key, value]);

  return [value, setValue];
}



export {useLocalState};