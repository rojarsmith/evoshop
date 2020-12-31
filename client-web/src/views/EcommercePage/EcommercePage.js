import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import log from 'loglevel';

export default function EcommercePage({ ...rest }) {
    log.info("[Home]: Rendering Home Component");
    return (
        <div>EcommercePage</div>
    );
}