# Simple STOMP based app

## Introduction
Create a simple STOMP based app that is a derivative of exhibits\02-simple-websocket-app\a-trigger-websocket-events\trigger-sayings-api

## Details of trigger-sayings-api
This app will expose a message based channel using the STOMP protocol on which a triggering message will be placed. In turn on processing this message the API will generate a common everyday saying and broadcast to all subscribers again using the STOMP protocol