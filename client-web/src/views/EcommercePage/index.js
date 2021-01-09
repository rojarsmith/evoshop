import React, { useEffect } from "react";
// import { useSelector } from "react-redux";
import { Grid, Hidden } from "@material-ui/core";
import log from "loglevel";
import DocumentTitle from "components/DocumentTitle";
import HorizontalSlider from "components/HorizontalSlider";
import Spinner from "components/Spinner";
import Header from "components/Header";
import "assets/styles/index.css";

export default function EcommercePage({ ...rest }) {
    const EcommercePageAPIData = {
        isLoading: true
    };

    useEffect(() => {
    }, []);

    // we will be showing spinner till we get the data via API
    if (EcommercePageAPIData.isLoading) {
        log.info("[Home]: loading")
        return <Spinner textComponent={
            <Grid container direction="column" spacing={1} style={{
                alignItems: "center", fontSize: "1.1rem", fontWeight: "bold"
            }}>
                <Grid item>... LOADING ...</Grid>
            </Grid>} />
    }

    log.info("[EcommercePage]: Rendering EcommercePage page.");
    return (
        <div>
            <DocumentTitle title={"Online Shopping for Women, Men, Fashion & Lifestyle - " + process.env.REACT_APP_SITE_NAME} />
            <Header />
            <Hidden only={['xs']}>
                <HorizontalSlider />
            </Hidden>
        </div>
    );
}