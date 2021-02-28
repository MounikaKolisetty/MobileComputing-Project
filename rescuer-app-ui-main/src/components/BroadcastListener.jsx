import React, { useCallback, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import AppStorage from '../service/appStorage';

const loginEventsChannel = new BroadcastChannel('login-events-channel');

function BroadcastListener() {
  const history = useHistory();
  const loginEventsListener = useCallback(
    (msg) => {
      if (msg.data === 'login') {
        AppStorage.setItem("access_token", "");
        history.push('login');
      }
    },
    [history],
  );

  useEffect(() => {
    const accessToken = AppStorage.getItem("access_token");
    if(!accessToken || accessToken === 'undefined') {
        history.push('login');
    }
  }, [history]);

  React.useEffect(() => {
    loginEventsChannel.addEventListener('message', loginEventsListener);
    return () => {
      loginEventsChannel.removeEventListener('message', loginEventsListener);
      loginEventsChannel.close();
    };
  }, [loginEventsListener]);

  return <></>;
}

export default BroadcastListener;