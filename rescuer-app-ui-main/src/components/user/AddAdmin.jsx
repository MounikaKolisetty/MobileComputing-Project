import React from 'react';
import AddUser from './AddUser';


function AddAdmin() {
    return <AddUser userType="ADMIN" URL={process.env.REACT_APP_ADD_ADMIN}/>
}

export default AddAdmin;