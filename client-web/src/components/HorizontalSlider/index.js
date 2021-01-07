import log from 'loglevel';
import Swiper from 'react-id-swiper';
import { horizontalSliderData1 as horizontalSliderData } from 'mock/data';
// import Box from '@material-ui/core/Box';

export default function HorizontalSlider(props) {
    const params = {
        spaceBetween: 30,
        centeredSlides: true,
        autoplay: {
            delay: 4000,
            disableOnInteraction: false
        },
        pagination: {
            el: '.swiper-pagination',
            clickable: true
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
        }
    }

    const renderImageList = (imageList) => {
        log.info(imageList);
        if (!imageList) {
            log.info(`[HorizontalSlider]: imageList is null`)
            // return; // TODO: Error area.
        }

        log.trace("[HorizontalSlider]: Rendering renderImageList imageList = " + JSON.stringify(imageList));
        return imageList.map(({ idx, imageURL, imageLocalPath }) => {
            log.trace(`[HorizontalSlider]: Rendering renderImageList imageList image = ${imageURL}`)
            return (
                <img key={idx} src={imageURL} alt={imageLocalPath} />
            )
        });
    };

    log.info("[HorizontalSlider]: Rendering HorizontalSlider Component")
    return (
        <Swiper {...params}>
            {renderImageList(horizontalSliderData())}
        </Swiper>
    )
}
