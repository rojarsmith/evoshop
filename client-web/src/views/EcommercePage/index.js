import React from "react";
// import { useSelector } from "react-redux";
import Hidden from "@material-ui/core/Hidden";
import log from 'loglevel';
import DocumentTitle from 'components/DocumentTitle';
import HorizontalSlider from "components/HorizontalSlider";
import 'assets/styles/index.css'
import Header from 'components/Header'

export default function EcommercePage({ ...rest }) {
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