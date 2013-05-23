/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

//because of bug in Array.prototype.min() where it thinks
//0 is not < 1, use 1 as teh starting point
var MIN_SLIDER_VALUE = 1;
//the value that reflects where the thumbnail is on the slider
var currentSliderValue = MIN_SLIDER_VALUE;
//the value that reflects the left thumbnail on the begin-end slider (yellow guy)
var beginSliderValue = MIN_SLIDER_VALUE;
//the value that reflects the right thumbnail on the begin-end slider
var endSliderValue;
//the greatest possible value that the thumbnail can have (N images)
var maxSliderValue;

//this stores the URLs to the images for the slideshow
var slideShowImagesArray;
//this is the actual element in the DOM of the slider for the slideshow
var slideShowSlider;
//this is the actual element in the DOM of the slider for the begin-end boundaries
var beginEndSlider;
//this is the id in the DOM of the place where the images should be set
var slideShowFrameElementId;

//time in ms between showing next image in series
var SLIDE_SHOW_LINGER_TIME = 500;
//forward, backward or blank depending on looping forward,back or not at all
var loopMode = '';
//this is the number returned by the timer
var playTimeoutId = -1;
//this guard stops playback from continuing until the last frame is completely loaded and shown
var lastImageLoaded = false;
//prefetchAll counts how many prefetched so it knows when done
var numPrefetched = 0;
//this is set by outside client that wants to know when prefetchAll is done (to drop progress indicator or whatever)
var finalPrefetchAllCallback;

/**
 * You guessed it - fetch every image in the array
 **/
function prefetchAll(theFinalPrefetchAllCallback) {
    finalPrefetchAllCallback = theFinalPrefetchAllCallback;
    numPrefetched = 0;
    for(var i=0;i<slideShowImagesArray.length;i++) {        
        loadImage(i, _prefetchAllCallback);
    }   
}

/**
 * This is used internally for prefetchAll to figure out when
 * done completely.  
 **/
function _prefetchAllCallback() {
    numPrefetched+=1;
    
    if(numPrefetched >= slideShowImagesArray.length) {
        finalPrefetchAllCallback();
    }
}

/**
 * fetch the image for the given offset within the image array
 **/
function loadImage(offset, loadCallback) {
    var prefetchImage = new Image();
    if(loadCallback) {
        prefetchImage.onload = loadCallback;
        
        //IE6 seems to crap out without these set?
        prefetchImage.onerror = function() {};
        prefetchImage.onabort = function() {};        
    }         
    //set src will cause the resource to be fetched by browser and stored in local cache
    prefetchImage.src = slideShowImagesArray[offset];
}
    
/**
 * Initialize the array of URLs to the frames of the slideshow.
 * Set the HTML DOM element that will be set with the images, and set
 * it to the first frame of the slideshow
 **/
function initSlideShow(theSlideShowImagesArray,
                       theSlideShowFrameElementId) {
    slideShowFrameElementId = theSlideShowFrameElementId;
    slideShowImagesArray = theSlideShowImagesArray;
    
    $(slideShowFrameElementId).onload = imageLoadCallback;
    
    //$ is scriptaculous function to deference the element with said name in the DOM
	$(slideShowFrameElementId).src = slideShowImagesArray[currentSliderValue-1];    
}

/**
 * Attach the DIV element for the slider to the Slider control (that
 * is constructed here) and attach the DIV elements for the handles.
 **/
function initSlider(sliderElementId, 
                    handles,
                    theMaxSliderValue,
                    beginEndSliderElementId,
                    beginEndHandles,
                    highlightedSpanElementId) {
                    
    maxSliderValue = theMaxSliderValue;
   
    //$R is scriptaculous function to create ObjectRange object
    var slideShowSliderOptions = { 
        range: $R(1, maxSliderValue),
        values: $R(1, maxSliderValue),
        alignX: 0
    };
 
    slideShowSlider = new Control.Slider(handles, 
                                         sliderElementId,
                                         slideShowSliderOptions);

   
    
    //do this setValue last so current handle is the active one and
    //user clicking in span will move this handle instead of end
    slideShowSlider.setValue(currentSliderValue);
    
    slideShowSlider.options.onSlide = 
    
    slideShowSlider.options.onChange = function(value) {

        //cap this out
        if(value < beginSliderValue) {
            value = beginSliderValue;
            slideShowSlider.setValue(value);
        }
        else
        if(value > endSliderValue) {
            value = endSliderValue;
            slideShowSlider.setValue(value);
        }
        else {
            currentSliderValue = value;
            
            //Jiffy.mark("LoadImage");
            lastImageLoaded = false;
            $(slideShowFrameElementId).src = slideShowImagesArray[currentSliderValue-1];
            //Jiffy.measure("Done", "LoadImage");
        }    
    };
   
    //$R is scriptaculous function to create ObjectRange object
    var beginEndSliderOptions = { 
        range: $R(1, maxSliderValue),
        values: $R(1, maxSliderValue),
        alignX: 0,
        restricted: true,
        spans: [highlightedSpanElementId]
    };
        
    beginEndSlider = new Control.Slider(beginEndHandles, 
                                        beginEndSliderElementId,
                                        beginEndSliderOptions); 
    beginEndSlider.setValue(1, 0);
    beginEndSlider.setValue(maxSliderValue, 1);
    endSliderValue = maxSliderValue;
    beginEndSlider.options.onChange = function(value) { 
           
        if(value[0] > currentSliderValue) {
            beginEndSlider.setValue(currentSliderValue, 0);
        }
        else
        if(value[1] < currentSliderValue) {
            beginEndSlider.setValue(currentSliderValue, 1);
        }        
        else {
            beginSliderValue = value[0];
            endSliderValue = value[1];
        }        
    };                                                                                  
}

function resetBeginEndSlider() {
    beginEndSlider.setValue(1, 0);
    beginEndSlider.setValue(maxSliderValue, 1);
}

function imageLoadCallback() {
    lastImageLoaded = true;
}


function stopPlayback() {
    if(playTimeoutId!=-1) {
        clearInterval(playTimeoutId);
    }
    loopMode = '';
}
                                      
   
function navigateToBegin() {
    stopPlayback();
    slideShowSlider.setValue(beginSliderValue);
}

function navigateToBeginQC() {
    stopPlayback();
    slideShowSlider.setValue(beginSliderValue);
    document.getElementById('moveImageForm:currentImgNum').value=beginSliderValue;
    document.getElementById('moveImageForm').submit();
}

function backOne() {
    stopPlayback();
    backOneImpl();
}

function backOneQC() {
 backOne();
 document.getElementById('moveImageForm:currentImgNum').value=currentSliderValue;
 document.getElementById('moveImageForm').submit();
}

function sliderPos() {
	document.getElementById('moveImageForm:currentImgNum').value=currentSliderValue;
	document.getElementById('moveImageForm').submit();
}

function backOneImpl() {
    if(currentSliderValue>beginSliderValue) {  
        currentSliderValue -= 1;
        slideShowSlider.setValue(currentSliderValue);
    }
    else { //this is harmless if -, and necessary if coming from play
        stopPlayback();
    }
}

function backOneImplQC() {
    if(currentSliderValue>beginSliderValue) {  
        currentSliderValue -= 1;
        slideShowSlider.setValue(currentSliderValue);
    }
    else { //this is harmless if -, and necessary if coming from play
        stopPlayback();
 		document.getElementById('moveImageForm:currentImgNum').value=currentSliderValue;
		document.getElementById('moveImageForm').submit();       
    }
}

function playBackward() {
    stopPlayback();  
    playTimeoutId = setInterval("backOneImpl();",
                                SLIDE_SHOW_LINGER_TIME);                              
}

function playBackwardQC() {
    stopPlayback();  
    playTimeoutId = setInterval("backOneImplQC();",
                                SLIDE_SHOW_LINGER_TIME);                              
}

function navigateToEnd() {
    stopPlayback();
    slideShowSlider.setValue(endSliderValue);
}

function navigateToEndQC() {
    stopPlayback();
    slideShowSlider.setValue(endSliderValue);
    document.getElementById('moveImageForm:currentImgNum').value=endSliderValue;
    document.getElementById('moveImageForm').submit();
}

function forwardOne() {
    stopPlayback();
    forwardOneImpl();  
}

function forwardOneQC() {
 forwardOne();
 document.getElementById('moveImageForm:currentImgNum').value=currentSliderValue;
 document.getElementById('moveImageForm').submit();
}


function forwardOneImpl() {
    if(currentSliderValue<endSliderValue) {
        if(lastImageLoaded) {
            currentSliderValue += 1;
            slideShowSlider.setValue(currentSliderValue);
        }    
    }  
    else { //this is harmless if +, and necessary if coming from play
        stopPlayback();           
    }
}

function forwardOneImplQC() {
    if(currentSliderValue<endSliderValue) {
        if(lastImageLoaded) {
            currentSliderValue += 1;
            slideShowSlider.setValue(currentSliderValue);
        }    
    }  
    else { //this is harmless if +, and necessary if coming from play
        stopPlayback();
        document.getElementById('moveImageForm:currentImgNum').value=currentSliderValue;
		document.getElementById('moveImageForm').submit();           
    }
}

function loopImpl() {
    if(loopMode == 'forward') {
        if(currentSliderValue<endSliderValue) {
            currentSliderValue += 1;
            slideShowSlider.setValue(currentSliderValue);
        }
        else {
            loopMode = 'backward';
            //wait for the next iteration to simplify logic
        }                     
    }
    else {
        if(currentSliderValue>beginSliderValue) {  
            currentSliderValue -= 1;
            slideShowSlider.setValue(currentSliderValue);
        }  
        else {
            loopMode = 'forward';
            //wait for the next iteration to simplify logic
        } 
    }
}

function playForward() {
    stopPlayback();  
    playTimeoutId = setInterval("forwardOneImpl();",
                                SLIDE_SHOW_LINGER_TIME);                            
}

function playForwardQC() {
    stopPlayback();  
    playTimeoutId = setInterval("forwardOneImplQC();",
                                SLIDE_SHOW_LINGER_TIME);                            
}


function loopBackAndForth() {
    stopPlayback();
    loopMode = 'forward';
    playTimeoutId = setInterval("loopImpl();",
                                SLIDE_SHOW_LINGER_TIME); 

}    