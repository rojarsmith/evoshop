import imageHS1 from './horizontal-slider/hs1-1920x504.jpg';
import imageHS2 from './horizontal-slider/hs2-1920x504.jpg';
import imageHS3 from './horizontal-slider/hs3-1920x504.jpg';

export const horizontalSliderData1 = (content = "Slide") => {
    const images = [
        {
            imageURL: imageHS1,
            imageLocalPath: imageHS1
        },
        {
            imageURL: imageHS2,
            imageLocalPath: imageHS2
        },
        {   
            imageURL: imageHS3,
            imageLocalPath: imageHS3
        }
    ];

    return images.map(({imageURL, imageLocalPath}, idx) => ({
        idx: idx + 1,
        imageURL,
        imageLocalPath
    }));
};

export const horizontalSliderData2 = (content = "Slide") => {
    const colors = [
        "#4a9eda",
        "#6872e0",
        "#9966e0",
        "#d665e0",
        "#e066ad",
        "#e16973",
        "#d7c938",
        "#da864a",
        "#96d73b",
        "#54da48",
        "#46da84",
        "#44d9cd"
    ];

    return colors.map((color, idx) => ({
        idx: idx + 1,
        content: `${content} #${idx + 1}`,
        color
    }));
};
