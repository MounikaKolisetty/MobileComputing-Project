import React from 'react';
import AddUser from './AddUser';


function AddRescuer() {
    return <AddUser userType="RESCUER" URL={process.env.REACT_APP_ADD_RESCUER}/>
}

export default AddRescuer;