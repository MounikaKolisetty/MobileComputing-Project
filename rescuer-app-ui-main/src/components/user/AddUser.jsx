import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import axios from '../Axios';

const useStyles = makeStyles((theme) => ({
    root: {
      '& .MuiTextField-root': {
        margin: theme.spacing(1),
        width: '25ch',
      },
      marginTop: theme.spacing(10),
    },
    submit: {
      margin: theme.spacing(3, 0, 2),
    },
  }));

const addUserInitData = {
    userName: '',
    password: '',
}

function AddUser(props) {
    const classes = useStyles();
    const [formFields, setFormFields] = useState({...addUserInitData});
    const [isPostingData, setIsPostingData] = React.useState(false);

    const onCreateUser = async () => {
        setIsPostingData(true);
        const requestData = {
            userName: formFields.userName,
            password: formFields.password,
            userType: props.userType,
            isUserActive: true,
        };
        try {
            const response = await axios.post(props.URL, requestData);
            if(response.status === 200 || response.status === 201) {
                alert('User Created!');
                setFormFields({...addUserInitData})
            }
        } catch(error) {
            alert("Something went wrong!");
        }
        setIsPostingData(false);
    }

    const onInputChange = (e) => {
        setFormFields({... formFields, [e.target.name]: e.currentTarget.value})
    }

    return (
        <div className={classes.root} >
            {isPostingData && <Typography variant="h8" component="h3">
                Loading...
                </Typography>}
            <div>
                <Typography variant="h8" component="h3">
                User Details
                </Typography>
                <TextField
                    required
                    id="outlined-required"
                    label="User Name"
                    name="userName"
                    variant="outlined"
                    value={formFields.userName}
                    onChange={onInputChange}
                />
            </div>
            <div>
                <TextField
                    required
                    type="password"
                    id="outlined-required"
                    label="Password"
                    name="password"
                    variant="outlined"
                    value={formFields.password}
                    onChange={onInputChange}
                />
            </div>
    
            <Button
                type="click"
                variant="contained"
                color="primary"
                onClick={onCreateUser}
                className={classes.submit}
            >
                Create User
            </Button>
        </div>
    )
}

export default AddUser;