import React from 'react';
import log from "loglevel";
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import { Badge } from "@material-ui/core";
// import { useSelector } from "react-redux";

export default function BadgeButton(props) {
    const addToCart = {
        totalQuantity: 10
    };

    log.info(`[BadgeButton]: Rendering BadgeButton Component`);
    return (
        <Badge badgeContent={addToCart.totalQuantity} color="secondary">
            <ShoppingCartIcon style={{ color: "black" }} />
        </Badge>
    );
}