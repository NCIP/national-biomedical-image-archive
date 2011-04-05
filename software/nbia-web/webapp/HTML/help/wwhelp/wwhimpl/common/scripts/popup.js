// Copyright (c) 2000-2003 Quadralay Corporation.  All rights reserved.
//

function  WWHPopup_Object(ParamThisPopupRef,
                          ParamWindowRef,
                          ParamPopupTranslateFunc,
                          ParamPopupFormatFunc,
                          ParamDivID,
                          ParamTextID,
                          ParamTimeout,
                          ParamOffsetX,
                          ParamOffsetY,
                          ParamWidth)
{
  this.mThisPopupRef = ParamThisPopupRef;
  this.mWindowRef    = ParamWindowRef;
  this.mDivID        = ParamDivID;
  this.mTextID       = ParamTextID;
  this.mTimeout      = (ParamTimeout > 0) ? ParamTimeout : 0;
  this.mOffsetX      = ParamOffsetX;
  this.mOffsetY      = ParamOffsetY;
  this.mWidth        = ParamWidth;


  // Updated when popup triggered
  //
  this.mbVisible     = false;
  this.mPositionX    = 0;
  this.mPositionY    = 0;
  this.mText         = "";
  this.mSetTimeoutID = null;

  this.fTranslate     = ParamPopupTranslateFunc;
  this.fFormat        = ParamPopupFormatFunc;
  this.fEventString   = WWHPopup_EventString;
  this.fDivTagText    = WWHPopup_DivTagText;
  this.fShow          = WWHPopup_Show;
  this.fPositionPopup = WWHPopup_PositionPopup;
  this.fPopup         = WWHPopup_Popup;
  this.fHide          = WWHPopup_Hide;
}

function  WWHPopup_EventString()
{
  var  EventString = "null";
  var  Browser = WWHFrame.WWHBrowser.mBrowser;


  // Set event string based on browser type
  //
  if ((Browser == 1) ||  // Shorthand for Netscape
      (Browser == 2) ||  // Shorthand for IE
      (Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
      (Browser == 5))    // Shorthand for Safari
  {
    EventString = "event";
  }
  else
  {
    EventString = "null";
  }

  return EventString;
}

function  WWHPopup_DivTagText()
{
  var  DivTagText = "";
  var  Browser = WWHFrame.WWHBrowser.mBrowser;
  var  VisibleAttribute = "visibility: hidden";


  // Update VisibleAttribute based on browser
  //
  if ((Browser == 2) ||  // Shorthand for Internet Explorer
      (Browser == 3) ||  // Shorthand for iCab
      (Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
      (Browser == 5))    // Shorthand for Safari
  {
    VisibleAttribute += " ; display: none";
  }

  // Open DIV tag
  //
  DivTagText += "<div id=\"" + this.mDivID + "\" style=\"position: absolute ; z-index: 1 ; " + VisibleAttribute + " ; top: 0px ; left: 0px\">\n";

  // Expand out popup in browsers that support innerHTML accessor
  //
  if ((Browser == 2) ||  // Shortcut for IE
      (Browser == 3) ||  // Shortcut for iCab
      (Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
      (Browser == 5))    // Shorthand for Safari
  {
    DivTagText += this.fFormat(this.mWidth, this.mTextID,
                               "Popup");
  }

  // Close out DIV tag
  //
  DivTagText += "</div>\n";

  return DivTagText;
}

function  WWHPopup_Show(ParamText,
                        ParamEvent)
{
  var  Browser = WWHFrame.WWHBrowser.mBrowser;
  var  bStartTimer = false;
  var  PopupDocument = eval(this.mWindowRef + ".document");
  var  TranslatedText;


  // Reset the timeout operation to display the popup
  //
  if (this.mSetTimeoutID != null)
  {
    clearTimeout(this.mSetTimeoutID);

    this.mSetTimeoutID = null;
  }

  // Check to see if there is anything to display
  //
  if ((ParamText != null) &&
      (ParamEvent != null))
  {
    if (Browser == 1)  // Shorthand for Netscape 4.x
    {
      this.mPositionX = ParamEvent.layerX;
      this.mPositionY = ParamEvent.layerY;

      this.mText = ParamText;

      bStartTimer = true;
    }
    else if (Browser == 2)  // Shorthand for IE
    {
      if ((typeof(PopupDocument.documentElement) != "undefined") &&
          (typeof(PopupDocument.documentElement.clientWidth) != "undefined") &&
          (typeof(PopupDocument.documentElement.clientHeight) != "undefined") &&
          ((PopupDocument.documentElement.scrollLeft != 0) ||
           (PopupDocument.documentElement.scrollTop != 0)))
      {
        this.mPositionX = PopupDocument.documentElement.scrollLeft + ParamEvent.x;
        this.mPositionY = PopupDocument.documentElement.scrollTop  + ParamEvent.y;
      }
      else
      {
        this.mPositionX = PopupDocument.body.scrollLeft + ParamEvent.x;
        this.mPositionY = PopupDocument.body.scrollTop  + ParamEvent.y;
      }

      // Workaround for IE 4.0 on Windows
      //
      if (WWHFrame.WWHBrowser.mbWindowsIE40)
      {
        this.mPositionX = ParamEvent.x;
        this.mPositionY = ParamEvent.y;
      }

      this.mText = ParamText;

      if (WWHFrame.WWHBrowser.mPlatform == 2)  // Shorthand for Macintosh
      {
        // Setting the position here before it is displayed
        // corrects a bug under IE 5 on the Macintosh
        //
        PopupDocument.all[this.mDivID].style.pixelLeft = 0;
        PopupDocument.all[this.mDivID].style.pixelTop  = 0;
        TranslatedText = this.fTranslate(this.mText);
        PopupDocument.all[this.mTextID].innerHTML = TranslatedText;
        this.fPositionPopup();
      }

      bStartTimer = true;
    }
    else if ((Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
             (Browser == 5))    // Shorthand for Safari
    {
      this.mPositionX = ParamEvent.layerX;
      this.mPositionY = ParamEvent.layerY;

      this.mText = ParamText;

      bStartTimer = true;
    }

    if (bStartTimer == true)
    {
      this.mSetTimeoutID = setTimeout(this.mThisPopupRef + ".fPopup()", this.mTimeout);
    }
  }
}

function  WWHPopup_PositionPopup()
{
  var  PopupWindow   = eval(this.mWindowRef);
  var  PopupDocument = eval(this.mWindowRef + ".document");
  var  Browser = WWHFrame.WWHBrowser.mBrowser;
  var  NewPositionX;
  var  NewPositionY;
  var  VisibleOffsetX;
  var  VisibleOffsetY;
  var  PopupWidth;
  var  PopupHeight;


  // Calculate new position for popup
  //
  NewPositionX = this.mPositionX + this.mOffsetX;
  NewPositionY = this.mPositionY + this.mOffsetY;

  if (Browser == 1)  // Shorthand for Netscape 4.x
  {
    // Attempt to determine DIV tag dimensions
    //
    PopupWidth = this.mWidth;
    if (PopupDocument.layers[this.mDivID].clip.width > PopupWidth)
    {
      PopupWidth = PopupDocument.layers[this.mDivID].clip.width;
    }
    PopupHeight = 60;  // Guess a value
    if (PopupDocument.layers[this.mDivID].clip.height > PopupHeight)
    {
      PopupHeight = PopupDocument.layers[this.mDivID].clip.height;
    }

    // Calculate maximum values for X and Y such that the
    // popup will remain visible
    //
    VisibleOffsetX = PopupWindow.innerWidth  - this.mOffsetX - PopupWidth;
    if (VisibleOffsetX < 0)
    {
      VisibleOffsetX = 0;
    }
    VisibleOffsetY = PopupWindow.innerHeight - this.mOffsetY - PopupHeight;
    if (VisibleOffsetY < 0)
    {
      VisibleOffsetY = 0;
    }

    // Confirm popup will be visible and adjust if necessary
    //
    if (NewPositionX > (PopupWindow.pageXOffset + VisibleOffsetX))
    {
      NewPositionX = PopupWindow.pageXOffset + VisibleOffsetX;
    }
    if (NewPositionY > (PopupWindow.pageYOffset + VisibleOffsetY))
    {
      NewPositionY = PopupWindow.pageYOffset + VisibleOffsetY;
    }

    // Set popup position
    //
    PopupDocument.layers[this.mDivID].left = NewPositionX;
    PopupDocument.layers[this.mDivID].top  = NewPositionY;
  }
  else if (Browser == 2)  // Shorthand for IE
  {
    // Attempt to determine DIV tag dimensions
    //
    PopupWidth = this.mWidth;
    if (PopupDocument.all[this.mDivID].offsetWidth > PopupWidth)
    {
      PopupWidth = PopupDocument.all[this.mDivID].offsetWidth;
    }
    PopupHeight = 60;  // Guess a value
    if (PopupDocument.all[this.mDivID].offsetHeight > PopupHeight)
    {
      PopupHeight = PopupDocument.all[this.mDivID].offsetHeight;
    }

    // Calculate maximum values for X and Y such that the
    // popup will remain visible
    //
    if ((typeof(PopupDocument.documentElement) != "undefined") &&
        (typeof(PopupDocument.documentElement.clientWidth) != "undefined") &&
        (typeof(PopupDocument.documentElement.clientHeight) != "undefined") &&
        ((PopupDocument.documentElement.clientWidth != 0) ||
         (PopupDocument.documentElement.clientHeight != 0)))
    {
      VisibleOffsetX = PopupDocument.documentElement.clientWidth  - this.mOffsetX - PopupWidth;
      VisibleOffsetY = PopupDocument.documentElement.clientHeight - this.mOffsetY - PopupHeight;
    }
    else
    {
      VisibleOffsetX = PopupDocument.body.clientWidth  - this.mOffsetX - PopupWidth;
      VisibleOffsetY = PopupDocument.body.clientHeight - this.mOffsetY - PopupHeight;
    }
    if (VisibleOffsetX < 0)
    {
      VisibleOffsetX = 0;
    }
    if (VisibleOffsetY < 0)
    {
      VisibleOffsetY = 0;
    }

    // Confirm popup will be visible and adjust if necessary
    //
    if ((typeof(PopupDocument.documentElement) != "undefined") &&
        (typeof(PopupDocument.documentElement.clientWidth) != "undefined") &&
        (typeof(PopupDocument.documentElement.clientHeight) != "undefined") &&
        ((PopupDocument.documentElement.scrollLeft != 0) ||
         (PopupDocument.documentElement.scrollTop != 0)))
    {
      if (NewPositionX > (PopupDocument.documentElement.scrollLeft + VisibleOffsetX))
      {
        NewPositionX = PopupDocument.documentElement.scrollLeft + VisibleOffsetX;
      }
      if (NewPositionY > (PopupDocument.documentElement.scrollTop + VisibleOffsetY))
      {
        NewPositionY = PopupDocument.documentElement.scrollTop + VisibleOffsetY;
      }
    }
    else
    {
      if (NewPositionX > (PopupDocument.body.scrollLeft + VisibleOffsetX))
      {
        NewPositionX = PopupDocument.body.scrollLeft + VisibleOffsetX;
      }
      if (NewPositionY > (PopupDocument.body.scrollTop + VisibleOffsetY))
      {
        NewPositionY = PopupDocument.body.scrollTop + VisibleOffsetY;
      }
    }

    // Set popup position
    //
    PopupDocument.all[this.mDivID].style.pixelLeft = NewPositionX;
    PopupDocument.all[this.mDivID].style.pixelTop  = NewPositionY;
  }
  else if ((Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
           (Browser == 5))    // Shorthand for Safari
  {
    // Attempt to determine DIV tag dimensions
    //
    PopupWidth = this.mWidth;
    if (PopupDocument.getElementById(this.mDivID).offsetWidth > PopupWidth)
    {
      PopupWidth = PopupDocument.getElementById(this.mDivID).offsetWidth;
    }
    PopupHeight = 60;  // Guess a value
    if (PopupDocument.getElementById(this.mDivID).offsetHeight > PopupHeight)
    {
      PopupHeight = PopupDocument.getElementById(this.mDivID).offsetHeight;
    }

    // Calculate maximum values for X and Y such that the
    // popup will remain visible
    //
    VisibleOffsetX = PopupWindow.innerWidth  - this.mOffsetX - PopupWidth;
    if (VisibleOffsetX < 0)
    {
      VisibleOffsetX = 0;
    }
    VisibleOffsetY = PopupWindow.innerHeight - this.mOffsetY - PopupHeight;
    if (VisibleOffsetY < 0)
    {
      VisibleOffsetY = 0;
    }

    // Confirm popup will be visible and adjust if necessary
    //
    if (NewPositionX > (PopupWindow.scrollX + VisibleOffsetX))
    {
      NewPositionX = PopupWindow.scrollX + VisibleOffsetX;
    }
    if (NewPositionY > (PopupWindow.scrollY + VisibleOffsetY))
    {
      NewPositionY = PopupWindow.scrollY + VisibleOffsetY;
    }

    // Set popup position
    //
    PopupDocument.getElementById(this.mDivID).style.left = NewPositionX + "px";
    PopupDocument.getElementById(this.mDivID).style.top  = NewPositionY + "px";
  }
}

function  WWHPopup_Popup()
{
  var  PopupDocument = eval(this.mWindowRef + ".document");
  var  Browser = WWHFrame.WWHBrowser.mBrowser;
  var  FormattedText;
  var  TranslatedText;


  if ((WWHFrame.WWHHandler.fIsReady()) &&
      (this.mSetTimeoutID != null))
  {
    if (Browser == 1)  // Shorthand for Netscape 4.x
    {
      // Format popup contents for browser
      //
      FormattedText = this.fFormat(this.mWidth, this.mTextID,
                                   this.fTranslate(this.mText));

      // Set popup contents
      //
      PopupDocument.layers[this.mDivID].document.open();
      PopupDocument.layers[this.mDivID].document.write(FormattedText);
      PopupDocument.layers[this.mDivID].document.close();

      // Position the popup
      //
      this.fPositionPopup();

      // Show the popup
      //
      PopupDocument.layers[this.mDivID].visibility = "visible";
      this.mbVisible = true;
    }
    else if ((Browser == 2) ||  // Shorthand for IE
             (Browser == 3))    // Shorthand for iCab
    {
      // Format popup contents for browser
      // Set popup contents
      //
      TranslatedText = this.fTranslate(this.mText);
      PopupDocument.all[this.mTextID].innerHTML = TranslatedText;

      // Position the popup
      //
      PopupDocument.all[this.mDivID].style.display = "block";
      this.fPositionPopup();

      // Show the popup
      //
      PopupDocument.all[this.mDivID].style.visibility = "visible";
      this.mbVisible = true;
    }
    else if ((Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
             (Browser == 5))    // Shorthand for Safari
    {
      // Format popup contents for browser
      // Set popup contents
      //
      TranslatedText = this.fTranslate(this.mText);
      PopupDocument.getElementById(this.mTextID).innerHTML = TranslatedText;

      // Initial popup positioning before object size can be determined
      //
      PopupDocument.getElementById(this.mDivID).style.display = "block";
      this.fPositionPopup();

      // Show the popup
      //
      PopupDocument.getElementById(this.mDivID).style.visibility = "visible";
      this.mbVisible = true;

      // Position the popup
      // Offset calculations may be off so we might need to reposition the popup
      //
      this.fPositionPopup();
    }
  }

  // Clear the setTimeout ID tracking field
  // to indicate that we're done.
  //
  this.mSetTimeoutID = null;
}

function  WWHPopup_Hide()
{
  var  Browser = WWHFrame.WWHBrowser.mBrowser;
  var  PopupDocument;


  // Cancel the setTimeout value that would have
  // displayed the popup
  //
  if (this.mSetTimeoutID != null)
  {
    clearTimeout(this.mSetTimeoutID);

    this.mSetTimeoutID = null;
  }

  // Shutdown the popup
  //
  if (this.mbVisible == true)
  {
    PopupDocument = eval(this.mWindowRef + ".document");

    if (Browser == 1)  // Shorthand for Netscape 4.x
    {
      PopupDocument.layers[this.mDivID].visibility = "hidden";
    }
    else if ((Browser == 2) ||  // Shorthand for IE
             (Browser == 3))    // Shorthand for iCab
    {
      PopupDocument.all[this.mDivID].style.visibility = "hidden";
      PopupDocument.all[this.mDivID].style.display    = "none";
    }
    else if ((Browser == 4) ||  // Shorthand for Netscape 6.0 (Mozilla)
             (Browser == 5))    // Shorthand for Safari
    {
      PopupDocument.getElementById(this.mDivID).style.visibility = "hidden";
      PopupDocument.getElementById(this.mDivID).style.display    = "none";
    }
  }

  this.mbVisible = false;
}
