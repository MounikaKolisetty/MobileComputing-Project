import Link from '@material-ui/core/Link';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Title from './Title';
import {useParams} from 'react-router-dom';
import React, { useEffect, useState } from "react";
import instance from '../Axios';
import { Button } from '@material-ui/core';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';

function preventDefault(event) {
  event.preventDefault();
}

const useStyles = makeStyles((theme) => ({
  seeMore: {
    marginTop: theme.spacing(3),
  },
  root: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
}));

export default function TicketDetails(props) {
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [ticketDetails, setTicketDetails] = useState([[]]);
  console.log(props);
  const { id } = useParams();
  console.log(id);
  useEffect(() => {
    instance.get(process.env.REACT_APP_TICKET_DETAILS+id)
      .then(
        (result) => {
          setTicketDetails(result.data);
          setIsLoaded(true);
        },
        (error) => {
          setIsLoaded(true);
          setError(error);
        }
      )
  }, [])
  function downloadAttachments(ticketId){
    instance.get(process.env.REACT_APP_DOWNLOAD_FILES+ticketId)
      .then(
        (response) => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'compressed.zip'); //or any other extension
          document.body.appendChild(link);
          link.click();
        },
        (error) => {
          setError(error);
        }
      )
  }
  const classes = useStyles();
  if(isLoaded){
    console.log(ticketDetails);
    return (
      <React.Fragment>
        <Title>Ticket Details</Title>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Ticket Id</TableCell>
              <TableCell>Ticket Status</TableCell>
              <TableCell>Allocated to</TableCell>
              <TableCell>created At</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow>
              <TableCell>{ticketDetails.ticketId}</TableCell>
              <TableCell>{ticketDetails.ticketStatus}</TableCell>
              <TableCell>{ticketDetails.allocatedTo}</TableCell>
              <TableCell>{ticketDetails.createdAt}</TableCell>
              <TableCell>
                <Button
                        type="button"
                        fullWidth
                        variant="contained"
                        color="primary"
                        onClick={()=>downloadAttachments(ticketDetails.ticketId)}
                    >
                    Download Attachments
                </Button>
               </TableCell>
            </TableRow>
          </TableBody>
        </Table>
        <Card className={classes.root} variant="outlined">
          <CardContent>
            <Typography className={classes.title} color="textSecondary" gutterBottom>
              Emergency Contact Details
            </Typography>
            <Typography variant="body2" component="p">
              EmergencyAddress: {ticketDetails.emergencyContactDetails.emergencyAddress}
            </Typography>
            <Typography variant="body2" component="p">
              EmergencyContactAge: {ticketDetails.emergencyContactDetails.emergencyContactAge}
            </Typography>
            <Typography variant="body2" component="p">
              EmergencyPhoneNumber: {ticketDetails.emergencyContactDetails.emergencyPhoneNumber}
            </Typography>
            <Typography variant="body2" component="p">
              Lat, Long: {ticketDetails.latitude}, {ticketDetails.longitude}
            </Typography>
          </CardContent>
        </Card>
        <Card className={classes.root} variant="outlined">
        <CardContent>
            <Typography className={classes.title} color="textSecondary" gutterBottom>
              Victim Details
            </Typography>
            <Typography variant="body2" component="p">
              Victim Name: {ticketDetails.victimDetails.victimName}
            </Typography>
            <Typography variant="body2" component="p">
              Victim Age: {ticketDetails.victimDetails.victimAge}
            </Typography>
            <Typography variant="body2" component="p">
              Victims Around: {ticketDetails.victimsAround}
            </Typography>
            <Typography variant="body2" component="p">
              Additional Information: {ticketDetails.additionalInformation}
            </Typography>
          </CardContent>
        </Card>
       </React.Fragment>
    );
  }else {
    return <div>Loading...</div>;
  }
  
}