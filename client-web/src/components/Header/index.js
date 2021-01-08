import React, { useEffect } from "react";
import { Link } from "react-router-dom";
// import { useSelector } from "react-redux";
import log from "loglevel";
import { AppBar, Toolbar, Typography, Grid } from '@material-ui/core';
import Hidden from "@material-ui/core/Hidden";
import Avatar from '@material-ui/core/Avatar';
import BadgeButton from 'components/BadgeButton';
import AccountCircle from '@material-ui/icons/AccountCircle';
import Fingerprint from "@material-ui/icons/Fingerprint";
import PersonAdd from "@material-ui/icons/PersonAdd";
import headerStyles from 'assets/styles/materialUI/headerStyles';

export default function Header(props) {
    const classes = headerStyles();
    // const [hamburgerButtonState, setHamburgerButtonState] = useState(false);

    // const { isSignedIn, tokenId, firstName } = useSelector(state => state.signInReducer);

    let authenticationIcon = null;
    let authenticationLabel = null;

    useEffect(() => {

    }, []);

    const changeAuthStatusHandler = () => {

    }

    const changePageToShoppingCartHandler = () => {
    }

    let fName = 'DevUser';
    authenticationIcon = <Avatar sizes="small"
        style={{
            width: 20, height: 20,
            backgroundColor: "orange",
            filter: "saturate(5)"
        }}>

        {fName.charAt(0).toUpperCase()}
    </Avatar>;
    authenticationIcon = <AccountCircle />;
    authenticationIcon = <Fingerprint />;
    authenticationLabel = "Sign In";

    const renderIndependentElem = (eventHandler, icon, label, paddingTop) => {
        return (
            <Grid item>
                <Grid container direction="column" alignItems="center"
                    onClick={eventHandler} style={{ cursor: 'pointer' }}>
                    <Grid item style={{ height: 21, width: 21, paddingTop: paddingTop }}>
                        {icon}
                    </Grid>
                    <Grid item style={{ color: "black", fontSize: "0.8rem", fontWeight: 'bold' }}>
                        {label}
                    </Grid>
                </Grid>
            </Grid>
        )
    }

    log.info(`[Head]: Rendering Head Component`)
    return (
        <>
            <div style={{ paddingBottom: 80 }}>
                <AppBar color="default" className={classes.appBarRoot}>
                    <Toolbar classes={{ root: classes.toolBarRoot }}>
                        <Grid container alignItems="center">
                            <Grid item>
                                <Link to="/">
                                    <Typography className={classes.title}>
                                        {process.env.REACT_APP_SITE_NAME}
                                    </Typography>
                                </Link>
                            </Grid>
                        </Grid>
                        <Hidden xsDown>
                            {renderIndependentElem(changeAuthStatusHandler, authenticationIcon, authenticationLabel, 2)}
                            {renderIndependentElem(null, <PersonAdd />, "Sign Up", 2)}
                            {renderIndependentElem(changePageToShoppingCartHandler, <BadgeButton />, "Cart", 0)}
                        </Hidden>
                    </Toolbar>
                </AppBar>
            </div>
        </>
    )
}