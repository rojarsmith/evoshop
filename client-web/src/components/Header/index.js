import React, { useEffect, useState } from "react";
import {Link} from "react-router-dom";
import log from "loglevel";
import { AppBar, Toolbar, IconButton, Typography, Grid } from '@material-ui/core';

export default function Header(props) {
    // const [hamburgerButtonState, setHamburgerButtonState] = useState(false);


    useEffect(() => {

    }, []);

    log.info(`[Head]: Rendering Head Component`)
    return (
        <>
            <div style={{ paddingBottom: 80 }}>
                <AppBar color="default">
                    <Toolbar>
                    <Grid container>
                    <Grid item>
                                <Link to="/">
                                    <Typography>
                                        {process.env.REACT_APP_SITE_NAME}
                                    </Typography>
                                </Link>
                            </Grid>
                    </Grid>
                    </Toolbar>
                </AppBar>
            </div>
        </>
    )
}