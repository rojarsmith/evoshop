import React from 'react';
import CircularProgress from '@material-ui/core/CircularProgress';
import { Box } from "@material-ui/core";

const initialObject = {
    textComponent: null
}

export default function Spinner(props = initialObject) {
    return (
        <Box display="flex" justifyContent="center" alignItems="center"
            css={{ minHeight: "60vh", flexDirection: "column" }}>

            <CircularProgress color="secondary" style={{ marginBottom: 20 }} />
            {props.textComponent}
        </Box>
    );
}