import log from 'loglevel';
import Swiper from 'react-id-swiper';

export default function HorizontalSlider(props) {
    const params = {
        spaceBetween: 30,
        centeredSlides: true
    }
    
    log.info("[HorizontalSlider]: Rendering HorizontalSlider Component")
    return (
        <Swiper {...params}>
        </Swiper>
    )
}
