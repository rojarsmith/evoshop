import React from 'react';
import { Helmet } from 'react-helmet-async';

export default function DocumentTitle(props) {
    const defaultTitle = process.env.REACT_APP_SITE_NAME;
    return (
        <Helmet>
            <title>{props.title ? props.title : defaultTitle}</title>
        </Helmet>
    );
}