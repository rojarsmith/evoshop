import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import log from 'loglevel';
import DocumentTitle from 'components/DocumentTitle';

export default function EcommercePage({ ...rest }) {
    log.info("[Home]: Rendering EcommercePage page.");
    return (
        <div>
            <DocumentTitle title={"Online Shopping for Women, Men, Fashion & Lifestyle - " + process.env.REACT_APP_SITE_NAME} />
            {process.env.REACT_APP_SITE_NAME}
            <br/>
            EcommercePage
        </div>
    );
}