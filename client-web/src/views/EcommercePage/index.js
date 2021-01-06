import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import Hidden from "@material-ui/core/Hidden";
import log from 'loglevel';
import DocumentTitle from 'components/DocumentTitle';
import HorizontalSlider from "./HorizontalSlider";

export default function EcommercePage({ ...rest }) {
    log.info("[EcommercePage]: Rendering EcommercePage page.");
    return (
        <div>
            <DocumentTitle title={"Online Shopping for Women, Men, Fashion & Lifestyle - " + process.env.REACT_APP_SITE_NAME} />
            {process.env.REACT_APP_SITE_NAME}
            <br/>
            EcommercePage
            <Hidden only={['xs']}>
                <HorizontalSlider/>
            </Hidden>
        </div>
    );
}