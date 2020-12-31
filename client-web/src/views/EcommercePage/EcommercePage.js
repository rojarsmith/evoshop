import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import log from 'loglevel';

export default function EcommercePage({ ...rest }) {
    log.info("[Home]: Rendering EcommercePage Component");
    return (
        <div>EcommercePage</div>
    );
}