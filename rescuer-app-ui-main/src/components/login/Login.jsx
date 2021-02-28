import React from 'react';
import { useHistory } from "react-router-dom";
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';

import axios from '../Axios';


import AppBar from '@material-ui/core/AppBar';
import IconButton from '@material-ui/core/IconButton';
import AddIcon from '@material-ui/icons/Add';
import AppStorage from '../../service/appStorage';
import paths from '../../routes/paths';

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {'Copyright © '}
      <Link color="inherit" href="https://material-ui.com/">
        Your Website
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

const signUpInitData = {userName:'', password: ''};

function SignInPage() {
  const classes = useStyles();
  const history = useHistory();
  const [formFileds, setFormFields] = React.useState({...signUpInitData});

  const onSignIn = async () => {
    const formData = {
      userName: formFileds.userName,
      password: formFileds.password,
    }

    try {
      const response = await axios.post(process.env.REACT_APP_LOGIN_URL, formData);
      const token = response.headers.authorization;
      const userType = response.headers.user_type;
      AppStorage.setItem('access_token', token);
      AppStorage.setItem('user_type', userType);
      history.push(paths.dashboard.path);
    } catch(error) {
      alert("Something went wrong!");
      console.error(error);
    }
  }

  const onFormChange = (e) => {
    setFormFields({...formFileds, [e.target.name]: e.currentTarget.value})
  }

  return (
    <Container component="main" maxWidth="xs">
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
        <div className={classes.form}>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            label="User Name"
            onChange={onFormChange}
            name="userName"
            autoFocus
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            onChange={onFormChange}
            label="Password"
            type="password"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            onClick={onSignIn}
          >
            Sign In
          </Button>
        </div>
      </div>
    </Container>
  )
}

const useAddTicketStyles = makeStyles((theme) => ({
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

const initData = {
  victimName: '',
  victimAge: '',
  requiredItems: '',
  emergencyContactAge: '',
  emergencyContactName: '',
  emergencyContactAddress: '',
  emergencyphoneNumber: '',
  additionalInformation: '',
  victimsAround: '',
  latitude: '',
  longitude: '',
}

function AddTicketPage() {
  const classes = useAddTicketStyles();

  const [formFields, setFormFields] = React.useState({...initData, attachments: []});

  const [isCreateTicketSubmitting, setCreateTicketSubmitting] = React.useState(false);

  const handleInput = (name, value) => {
    setFormFields({...formFields, [name]: value})
  };

  const saveTicket = async (latitude, longitude) => {
    setCreateTicketSubmitting(true);
    const createTicketPostData = {
      emergencyDetails: {
        emergencyContactName: formFields.emergencyContactName,
        emergencyContactAge: formFields.emergencyContactAge,
        emergencyPhoneNumber: formFields.emergencyphoneNumber,
        emergencyAddress: formFields.emergencyContactAddress
      },
      victimDetails: {
        victimName: formFields.victimName,
        victimAge: formFields.victimAge,
        requiredItems: [formFields.requiredItems],
        latitude: latitude,
        longitude: longitude
      },
      victimsAround: formFields.victimsAround,
      additionalInformation: formFields.additionalInformation
    };
    const createTicketObject = new FormData();
    createTicketObject.append('ticketData', JSON.stringify(createTicketPostData));
    formFields.attachments.forEach(file => {
      createTicketObject.append('files', file);
    });

    try {
      const response = await axios.post(process.env.REACT_APP_CREATE_TICKET, createTicketObject, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      if(response.data) {
        setFormFields({...initData, attachments: []});
        alert('Success');
      }
    } catch(error) {
      alert('Somthing went wrong!');
      console.error(error);
    }
    setCreateTicketSubmitting(false);
  }

  const trackPosition = (position) => {
    const latitude = position.coords.latitude; 
    const longitude = position.coords.longitude;
    saveTicket(latitude, longitude);
  }

  const getLocations = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(trackPosition);
    } else { 
      alert(`Please Accept, else you can't proceed`);
    }
  }

  const onCreateTicket = () => {
    getLocations();
  }

  const uploadAttachment = (e) => {
    formFields.attachments.push(e.currentTarget.files[0]);
  }

  return(
      <div className={classes.root} >
        {isCreateTicketSubmitting && <Typography variant="h8" component="h3">
              Form Submitting
            </Typography>}
          <div>
            <Typography variant="h8" component="h3">
              Victim Details
            </Typography>
            <TextField
              required
              id="outlined-required"
              label="Victim Name"
              name="victimName"
              variant="outlined"
              value={formFields.victimName}
              onChange={(e) => handleInput('victimName', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Victim Age"
              name="victimAge"
              variant="outlined"
              value={formFields.victimAge}
              onChange={(e) => handleInput('victimAge', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Required Items"
              name="requiredItems"
              variant="outlined"
              value={formFields.requiredItems}
              onChange={(e) => handleInput('requiredItems', e.currentTarget.value)}
            />
          </div>

          <div>
            <Typography variant="h8" component="h3">
              Emergency Details
            </Typography>
            <TextField
              required
              id="outlined-required"
              label="Contact Age"
              name="emergencyContactAge"
              variant="outlined"
              value={formFields.emergencyContactAge}
              onChange={(e) => handleInput('emergencyContactAge', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Contact Name"
              name="emergencyContactName"
              variant="outlined"
              value={formFields.emergencyContactName}
              onChange={(e) => handleInput('emergencyContactName', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Contact Address"
              name="emergencyContactAddress"
              variant="outlined"
              value={formFields.emergencyContactAddress}
              onChange={(e) => handleInput('emergencyContactAddress', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Phone Number"
              name="emergencyphoneNumber"
              variant="outlined"
              value={formFields.emergencyphoneNumber}
              onChange={(e) => handleInput('emergencyphoneNumber', e.currentTarget.value)}
            />
          </div>
          <div>
          <Typography variant="h8" component="h3">
              Additional Information
            </Typography>
            <TextField
              required
              id="outlined-required"
              label="Additional Information"
              name="additionalInformation"
              variant="outlined"
              value={formFields.additionalInformation}
              onChange={(e) => handleInput('additionalInformation', e.currentTarget.value)}
            />
            <TextField
              required
              id="outlined-required"
              label="Victims Around"
              name="victimsAround"
              variant="outlined"
              value={formFields.victimsAround}
              onChange={(e) => handleInput('victimsAround', e.currentTarget.value)}
            />
            <Button
              variant="contained"
              component="label"
            >
              Upload Attachment
              <input
                type="file"
                onChange={uploadAttachment}
                hidden
              />
            </Button>
          </div>
          <Button
            type="click"
            variant="contained"
            color="primary"
            onClick={onCreateTicket}
            className={classes.submit}
          >
            Create Ticket
          </Button>
      </div>
  )
}

export default function HomePage() {

  const [isAddTicketPageEnabled, setAddTicketPageEnabled] = React.useState(false);

  const goToAddTicketPage = () => {
    setAddTicketPageEnabled(true);
  };

  return (
    <React.Fragment>
      <AppBar position="absolute" className="height-4em">
        <IconButton color="inherit" className="ticket-add-icon">
            <AddIcon onClick={goToAddTicketPage} />
        </IconButton>
      </AppBar>
      <Container component="main" maxWidth="lg">
        <CssBaseline />
          {!isAddTicketPageEnabled && <SignInPage /> }
          {isAddTicketPageEnabled && <AddTicketPage /> }
        <Box mt={8}>
          <Copyright />
        </Box>
      </Container>
    </React.Fragment>
  );
}