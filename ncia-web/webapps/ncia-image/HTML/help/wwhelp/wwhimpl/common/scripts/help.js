// Copyright (c) 2000-2005 Quadralay Corporation.  All rights reserved.
//

function  WWHHelp_Object(ParamURL)
{
  var  URLParams;


  this.mbInitialized        = false;
  this.mbAccessible         = false;
  this.mInitialTabName      = null;
  this.mNewTabName          = null;
  this.mInitStage           = 0;
  this.mSettings            = new WWHCommonSettings_Object();
  this.mMessages            = new WWHCommonMessages_Object();
  this.mDocumentLoaded      = null;
  this.mLocationURL         = WWHFrame.WWHBrowser.fNormalizeURL(ParamURL);
  this.mBaseURL             = WWHStringUtilities_GetBaseURL(this.mLocationURL);
  this.mHelpURLPrefix       = WWHFrame.WWHBrowser.fRestoreEscapedSpaces(this.mBaseURL);
  this.mContextDir          = null;
  this.mTopicTag            = null;
  this.mDocumentURL         = "";
  this.mPopup               = null;
  this.mPopupContext        = "";
  this.mPopupLink           = "";
  this.mBookGroups          = new WWHBookGroups_Object();
  this.mBooks               = new WWHBookList_Object();
  this.mFavoritesCookie     = "WWH" + this.mSettings.mCookiesID + "_Favs";
  this.mbIgnoreNextKeyPress = false;
  this.mbAltKeyDown         = false;
  this.mAccessKey           = -1;
  this.mbAutoSyncTOC        = false;
  this.mbAlwaysSyncTOC      = true;

  this.fSingleTopic                    = WWHHelp_SingleTopic;
  this.fGetFrameReference              = WWHHelp_GetFrameReference;
  this.fSetLocation                    = WWHHelp_SetLocation;
  this.fReplaceLocation                = WWHHelp_ReplaceLocation;
  this.fReloadLocation                 = WWHHelp_ReloadLocation;
  this.fGetURLParameters               = WWHHelp_GetURLParameters;
  this.fCookiesEnabled                 = WWHHelp_CookiesEnabled;
  this.fInitStage                      = WWHHelp_InitStage;
  this.fHandlerInitialized             = WWHHelp_HandlerInitialized;
  this.fGetFrameName                   = WWHHelp_GetFrameName;
  this.fSetFrameName                   = WWHHelp_SetFrameName;
  this.fSetDocumentFrameWithURL        = WWHHelp_SetDocumentFrameWithURL;
  this.fSetDocumentFrame               = WWHHelp_SetDocumentFrame;
  this.fSetDocumentHREF                = WWHHelp_SetDocumentHREF;
  this.fGetBookIndexFileIndexURL       = WWHHelp_GetBookIndexFileIndexURL;
  this.fDetermineContextDocument       = WWHHelp_DetermineContextDocument;
  this.fLoadTopicData                  = WWHHelp_LoadTopicData;
  this.fProcessTopicResult             = WWHHelp_ProcessTopicResult;
  this.fDisplayContextDocument         = WWHHelp_DisplayContextDocument;
  this.fSetContextDocument             = WWHHelp_SetContextDocument;
  this.fGetBookFileHREF                = WWHHelp_GetBookFileHREF;
  this.fHREFToBookIndexFileIndexAnchor = WWHHelp_HREFToBookIndexFileIndexAnchor;
  this.fGetSyncPrevNext                = WWHHelp_GetSyncPrevNext;
  this.fHREFToTitle                    = WWHHelp_HREFToTitle;
  this.fEscapeHTML                     = WWHHelp_EscapeHTML;
  this.fPopupHTML                      = WWHHelp_PopupHTML;
  this.fShowPopup                      = WWHHelp_ShowPopup;
  this.fPopupLoaded                    = WWHHelp_PopupLoaded;
  this.fHidePopup                      = WWHHelp_HidePopup;
  this.fClickedPopup                   = WWHHelp_ClickedPopup;
  this.fDisplayFile                    = WWHHelp_DisplayFile;
  this.fDisplayFirst                   = WWHHelp_DisplayFirst;
  this.fShowTopic                      = WWHHelp_ShowTopic;
  this.fUpdate                         = WWHHelp_Update;
  this.fSyncTOC                        = WWHHelp_SyncTOC;
  this.fFavoritesCurrent               = WWHHelp_FavoritesCurrent;
  this.fDocumentBookkeeping            = WWHHelp_DocumentBookkeeping;
  this.fAutoSyncTOC                    = WWHHelp_AutoSyncTOC;
  this.fUnload                         = WWHHelp_Unload;
  this.fIgnoreNextKeyPress             = WWHHelp_IgnoreNextKeyPress;
  this.fHandleKeyDown                  = WWHHelp_HandleKeyDown;
  this.fHandleKeyPress                 = WWHHelp_HandleKeyPress;
  this.fHandleKeyUp                    = WWHHelp_HandleKeyUp;
  this.fProcessAccessKey               = WWHHelp_ProcessAccessKey;
  this.fFocus                          = WWHHelp_Focus;

  // Load up messages
  //
  this.mMessages.fSetByLocale(WWHFrame.WWHBrowser.mLocale);

  // Set cookie path
  //
  WWHFrame.WWHBrowser.fSetCookiePath(WWHStringUtilities_GetBaseURL(ParamURL));

  // Check URL parameters
  //
  URLParams = this.fGetURLParameters(this.mLocationURL);

  // Set accessibility flag
  //
  if (this.mSettings.mAccessible == "true")
  {
    this.mbAccessible = true;
  }
  else
  {
    if (URLParams[4] != null)
    {
      if (URLParams[4] == "true")
      {
        this.mbAccessible = true;
      }
    }
  }

  // Determine initial tab
  //
  if (URLParams[5] != null)
  {
    this.mInitialTabName = URLParams[5];
  }

  // Set popup capabilities
  //
  if (this.mbAccessible)
  {
    WWHFrame.WWHBrowser.mbSupportsPopups = false;
    WWHFrame.WWHBrowser.mbSupportsIFrames = false;
  }

  // Create popup
  //
  this.mPopup = new WWHPopup_Object("WWHFrame.WWHHelp.mPopup",
                                    this.fGetFrameReference("WWHDocumentFrame"),
                                    WWHPopupFormat_Translate,
                                    WWHPopupFormat_Format,
                                    "WWHPopupDIV", "WWHPopupText", 500, 12, 20,
                                    this.mSettings.mPopup.mWidth);
}

function  WWHHelp_SingleTopic()
{
  var  bVarSingleTopic = false;


  if (this.mLocationURL.indexOf("wwhelp/wwhimpl/common/html/wwhelp.htm") != -1)
  {
    bVarSingleTopic = true;
  }

  return bVarSingleTopic;
}

function  WWHHelp_GetFrameReference(ParamFrameName)
{
  var  VarFrameReference;


  switch (ParamFrameName)
  {
    case "WWHFrame":
      // WWHFrame
      //
      VarFrameReference = "WWHFrame";
      break;

    case "WWHNavigationFrame":
      // WWHFrame.WWHNavigationFrame
      //
      VarFrameReference = "WWHFrame.frames[0]";
      break;

    case "WWHTabsFrame":
    case "WWHPanelFrame":
    case "WWHPanelNavigationFrame":
    case "WWHPanelViewFrame":
      // WWHFrame.WWHNavigationFrame.WWHTabsFrame
      //
      // WWHFrame.WWHNavigationFrame.WWHPanelFrame
      //
      // WWHFrame.WWHNavigationFrame.WWHPanelFrame.WWHPanelNavigationFrame
      //
      // WWHFrame.WWHNavigationFrame.WWHPanelFrame.WWHPanelViewFrame
      //
      VarFrameReference = WWHFrame.WWHHandler.fGetFrameReference(ParamFrameName);
      break;

    case "WWHContentFrame":
      // WWHFrame.WWHContentFrame
      //
      if (this.fSingleTopic())
      {
        VarFrameReference = "WWHFrame";
      }
      else
      {
        VarFrameReference = "WWHFrame.frames[1]";
      }
      break;

    case "WWHPageNavFrame":
      // WWHFrame.WWHContentFrame.WWHPageNavFrame
      //
      VarFrameReference = this.fGetFrameReference("WWHContentFrame") + ".frames[0]";
      break;

    case "WWHControlsLeftFrame":
      // WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsLeftFrame
      //
      VarFrameReference = this.fGetFrameReference("WWHPageNavFrame") + ".frames[0]";
      break;

    case "WWHTitleFrame":
      // WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHTitleFrame
      //
      VarFrameReference = this.fGetFrameReference("WWHPageNavFrame") + ".frames[1]";
      break;

    case "WWHControlsRightFrame":
      // WWHFrame.WWHContentFrame.WWHPageNavFrame.WWHControlsRightFrame
      //
      VarFrameReference = this.fGetFrameReference("WWHPageNavFrame") + ".frames[2]";
      break;

    case "WWHDocumentFrame":
      // WWHFrame.WWHContentFrame.WWHDocumentFrame
      //
      VarFrameReference = this.fGetFrameReference("WWHContentFrame") + ".frames[1]";
      break;

    default:
      VarFrameReference = null;
      break;
  }

  return VarFrameReference;
}

function  WWHHelp_SetLocation(ParamFrame,
                              ParamURL)
{
  var  VarFrameReference;


  VarFrameReference = this.fGetFrameReference(ParamFrame);
  WWHFrame.WWHBrowser.fSetLocation(VarFrameReference, ParamURL);
}

function  WWHHelp_ReplaceLocation(ParamFrame,
                                  ParamURL)
{
  var  VarFrameReference;


  VarFrameReference = this.fGetFrameReference(ParamFrame);
  WWHFrame.WWHBrowser.fReplaceLocation(VarFrameReference, ParamURL);
}

function  WWHHelp_ReloadLocation(ParamFrame)
{
  var  VarFrameReference;


  VarFrameReference = this.fGetFrameReference(ParamFrame);
  WWHFrame.WWHBrowser.fReloadLocation(VarFrameReference);
}

function  WWHHelp_GetURLParameters(ParamURL)
{
  var  URLParams = new Array(null, null, null, null, null, null);
  var  Parts;
  var  ContextMarker    = "context=";
  var  TopicMarker      = "topic=";
  var  FileMarker       = "file=";
  var  HREFMarker       = "href=";
  var  AccessibleMarker = "accessible=";
  var  TabMarker        = "tab=";
  var  MaxIndex;
  var  Index;


  // Check for possible context specification
  //
  if (ParamURL.indexOf("?") != -1)
  {
    // Get parameters
    //
    Parts = ParamURL.split("?");
    Parts[0] = Parts[1];
    Parts.length = 1;
    if (Parts[0].indexOf("&") != -1)
    {
      Parts = Parts[0].split("&");
    }

    // Process parameters
    //
    for (MaxIndex = Parts.length, Index = 0 ; Index < MaxIndex ; Index++)
    {
      if (Parts[Index].indexOf(ContextMarker) == 0)
      {
        URLParams[0] = Parts[Index].substring(ContextMarker.length, Parts[Index].length);
      }
      if (Parts[Index].indexOf(TopicMarker) == 0)
      {
        URLParams[1] = Parts[Index].substring(TopicMarker.length, Parts[Index].length);
      }
      if (Parts[Index].indexOf(FileMarker) == 0)
      {
        URLParams[2] = Parts[Index].substring(FileMarker.length, Parts[Index].length);
      }
      if (Parts[Index].indexOf(HREFMarker) == 0)
      {
        URLParams[3] = Parts[Index].substring(HREFMarker.length, Parts[Index].length);
      }
      if (Parts[Index].indexOf(AccessibleMarker) == 0)
      {
        URLParams[4] = Parts[Index].substring(AccessibleMarker.length, Parts[Index].length);
      }
      if (Parts[Index].indexOf(TabMarker) == 0)
      {
        URLParams[5] = Parts[Index].substring(TabMarker.length, Parts[Index].length);
      }
    }

    // Make certain we have both a ContextTag and either a TopicTag or FileTag
    // Otherwise, reset them
    //
    if ((URLParams[0] == null) ||
        ((URLParams[1] == null) &&
         (URLParams[2] == null)))
    {
      URLParams[0] = null;
      URLParams[1] = null;
      URLParams[2] = null;
    }
  }

  return URLParams;
}

function  WWHHelp_CookiesEnabled()
{
  var  bVarEnabled;


  bVarEnabled = false;
  if ((WWHFrame.WWHHelp.mSettings.mbCookies) &&
      (WWHFrame.WWHBrowser.fCookiesEnabled()))
  {
    bVarEnabled = true;
  }

  return bVarEnabled;
}

function  WWHHelp_InitStage(ParamStage)
{
  if (( ! this.mbInitialized) &&
      (ParamStage == this.mInitStage))
  {
    // Perform actions for current init stage
    //
    switch (this.mInitStage)
    {
      case 0:  // Start initialization process
        // Alert the user if this browser is unsupported
        //
        if (WWHFrame.WWHBrowser.mbUnsupported)
        {
          alert(WWHFrame.WWHHelp.mMessages.mBrowserNotSupported);
        }

        this.fReplaceLocation("WWHControlsLeftFrame", this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init0.htm");
        break;

      case 1:  // Prep book data
        this.fReplaceLocation("WWHControlsLeftFrame", this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init1.htm");
        break;

      case 2:  // Load book data
        this.fReplaceLocation("WWHControlsLeftFrame", this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init2.htm");
        break;

      case 3:  // Handler setup
        // Initialize handler
        //
        WWHFrame.WWHHandler.fInit();
        break;

      case 4:  // Display controls
        // Preload graphics
        //
        WWHHelpUtilities_PreloadGraphics();

        // Initialize controls
        //
        WWHFrame.WWHControls.fInitialize();
        break;

      case 5:  // Display document
        this.fSetDocumentFrame();
        this.mbInitialized = true;

        // Set frame names for accessibility
        //
        if (this.mbAccessible)
        {
          WWHFrame.WWHHelp.fSetFrameName("WWHControlsLeftFrame");
          WWHFrame.WWHHelp.fSetFrameName("WWHTitleFrame");
          WWHFrame.WWHHelp.fSetFrameName("WWHControlsRightFrame");
          WWHFrame.WWHHelp.fSetFrameName("WWHDocumentFrame");
        }

        // Finalize hander
        //
        WWHFrame.WWHHandler.fFinalize();
        break;
    }

    // Increment stage
    //
    this.mInitStage++;
  }

  return 0;
}

function  WWHHelp_HandlerInitialized()
{
  if (WWHFrame.WWHHelp.mInitStage > 0)
  {
    if (WWHFrame.WWHHandler.mbInitialized)
    {
      this.fReplaceLocation("WWHControlsRightFrame", this.mHelpURLPrefix + "wwhelp/wwhimpl/common/html/init3.htm");
    }
  }
}

function  WWHHelp_GetFrameName(ParamFrameName)
{
  var  VarName;


  // Determine name for this frame
  //
  VarName = null;
  switch (ParamFrameName)
  {
    case "WWHFrame":
      // Nothing to do
      //
      break;

    case "WWHNavigationFrame":
      // Nothing to do
      //
      break;

    case "WWHTabsFrame":
    case "WWHPanelFrame":
    case "WWHPanelNavigationFrame":
    case "WWHPanelViewFrame":
      VarName = WWHFrame.WWHHandler.fGetFrameName(ParamFrameName);
      break;

    case "WWHContentFrame":
      // Nothing to do
      //
      break;

    case "WWHPageNavFrame":
      // Nothing to do
      //
      break;

    case "WWHControlsLeftFrame":
      VarName = WWHStringUtilities_EscapeHTML(WWHFrame.WWHControls.fLeftFrameTitle());
      break;

    case "WWHTitleFrame":
      VarName = "";
      break;

    case "WWHControlsRightFrame":
      VarName = WWHStringUtilities_EscapeHTML(WWHFrame.WWHControls.fRightFrameTitle());
      break;

    case "WWHDocumentFrame":
      VarName = WWHStringUtilities_EscapeHTML(WWHFrame.WWHHelp.mMessages.mAccessibilityDocumentFrameName);
      break;
  }
}

function  WWHHelp_SetFrameName(ParamFrameName)
{
  var  VarName;
  var  VarFrame;


  if (WWHFrame.WWHBrowser.mbSupportsFrameRenaming)
  {
    // Get frame name
    //
    VarName = this.fGetFrameName(ParamFrameName);
    if (VarName != null)
    {
      // Set frame name
      //
      VarFrame = eval(this.fGetFrameReference(ParamFrameName));
      VarFrame.name = VarName;
    }
  }
}

function  WWHHelp_SetDocumentFrameWithURL(ParamURL)
{
  var  VarURLParameters;
  var  VarParts;
  var  VarLocationURLNoParams;
  var  VarNewLocationURL;


  // Preserve URL parameter info
  //
  VarURLParameters = "";
  VarParts = ParamURL.split("?");
  if ((VarParts.length > 1) &&
      (VarParts[1].length > 0))
  {
    VarURLParameters = VarParts[1];
  }

  // Determine location URL
  //
  VarParts = this.mLocationURL.split("?");
  VarLocationURLNoParams = VarParts[0];

  // Build new location URL
  //
  VarNewLocationURL = VarLocationURLNoParams + "?" + VarURLParameters;

  // Update location and redirect
  //
  this.mLocationURL = VarNewLocationURL;
  this.fSetDocumentFrame();
}

function  WWHHelp_SetDocumentFrame()
{
  var  DocumentLoaded;
  var  ContextDocumentURL;
  var  bVarReplace;
  var  VarDocumentFrame;


  // Preserve current document if user clicked forward or back to see it
  //
  if (this.mDocumentLoaded != null)
  {
    DocumentLoaded = this.mDocumentLoaded;

    this.mDocumentLoaded = null;
    this.fUpdate(DocumentLoaded);
  }
  else
  {
    // Replace document frame if "blank.htm" currently displayed
    //
    bVarReplace = false;
    VarDocumentFrame = eval(this.fGetFrameReference("WWHDocumentFrame"));
    if (VarDocumentFrame.location.href.indexOf("wwhelp/wwhimpl/common/html/blank.htm") != -1)
    {
      bVarReplace = true;
    }

    // Display document or determine correct document to display
    //
    ContextDocumentURL = this.fDetermineContextDocument();
    if (ContextDocumentURL != null)
    {
      this.fSetDocumentHREF(ContextDocumentURL, bVarReplace);
    }
    else  // Load topic data to determine document to display
    {
      this.fSetDocumentHREF(this.mBaseURL + "wwhelp/wwhimpl/common/html/document.htm", bVarReplace);
    }
  }
}

function  WWHHelp_SetDocumentHREF(ParamURL,
                                  bParamReplace)
{
  var  RestoredURL;


  if (ParamURL.length > 0)
  {
    RestoredURL = WWHFrame.WWHBrowser.fRestoreEscapedSpaces(ParamURL);

    if (bParamReplace)
    {
      this.fReplaceLocation("WWHDocumentFrame", RestoredURL);
    }
    else
    {
      this.fSetLocation("WWHDocumentFrame", RestoredURL);
    }
  }
}

function  WWHHelp_GetBookIndexFileIndexURL(ParamBookIndex,
                                           ParamFileIndex,
                                           ParamAnchor)
{
  var  URL = "";
  var  BookListEntry;


  if ((ParamBookIndex >= 0) &&
      (ParamFileIndex >= 0))
  {
    BookListEntry = this.mBooks.mBookList[ParamBookIndex];

    URL = this.mBaseURL + BookListEntry.mDirectory + BookListEntry.mFiles.fFileIndexToHREF(ParamFileIndex);
    if ((typeof(ParamAnchor) != "undefined") &&
        (ParamAnchor != null) &&
        (ParamAnchor.length > 0))
    {
      URL += "#" + ParamAnchor;
    }
  }

  return URL;
}

function  WWHHelp_DetermineContextDocument()
{
  var  ContextDocumentURL = null;
  var  URLParams          = this.fGetURLParameters(this.mLocationURL);
  var  ContextBook;


  // Automatically synchronize TOC
  //
  this.mbAutoSyncTOC = true;

  // Check for context specification
  //
  if (URLParams[3] != null)  // href specified
  {
    ContextDocumentURL = this.mBaseURL + URLParams[3];
  }
  else if (URLParams[0] != null)  // context specified
  {
    // Determine book directory
    //
    ContextBook = this.mBooks.fGetContextBook(URLParams[0]);
    if (ContextBook != null)
    {
      if (URLParams[2] != null)  // file specified
      {
        ContextDocumentURL = this.mBaseURL + ContextBook.mDirectory + URLParams[2];
      }
      else if (URLParams[1] != null)  // topic specified
      {
        // Setup for a topic search
        //
        this.mContextDir = ContextBook.mDirectory;
        this.mTopicTag   = URLParams[1];

        this.mDocumentURL = "";
      }
    }
    else  // Display splash page if nothing else found
    {
      ContextDocumentURL = this.mBaseURL + "wwhelp/wwhimpl/common/html/default.htm";
    }
  }
  else  // Display splash page if nothing else found
  {
    ContextDocumentURL = this.mBaseURL + "wwhelp/wwhimpl/common/html/default.htm";
  }

  return ContextDocumentURL;
}

function  WWHHelp_LoadTopicData()
{
  var  LoadTopicDataHTML = "";
  var  VarParameters;


  // Workaround Safari reload bug
  //
  VarParameters = "";
  if (WWHFrame.WWHBrowser.mBrowser == 5)  // Shorthhand for Safari
  {
    VarParameters = "?" + (new Date() * 1);
  }

  LoadTopicDataHTML += "<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + this.mHelpURLPrefix + WWHFrame.WWHBrowser.fRestoreEscapedSpaces(this.mContextDir) + "wwhdata/common/topics.js" + VarParameters + "\"></script>";
  LoadTopicDataHTML += "<script type=\"text/javascript\" language=\"JavaScript1.2\" src=\"" + this.mHelpURLPrefix + "wwhelp/wwhimpl/common/scripts/documt1s.js" + VarParameters + "\"></script>";

  return LoadTopicDataHTML;
}

function  WWHHelp_ProcessTopicResult(ParamTopicURL)
{
  if (ParamTopicURL != null)
  {
    this.mDocumentURL = this.mBaseURL + this.mContextDir + ParamTopicURL;
  }
}

function  WWHHelp_DisplayContextDocument()
{
  WWHFrame.WWHHelp.fSetDocumentHREF(this.mDocumentURL, true);
}

function  WWHHelp_GetURLPrefix(ParamURL)
{
  var  URLPrefix  = null;
  var  WorkingURL = "";
  var  Parts;
  var  Index;


  // Standardize URL for processing
  //
  WorkingURL = ParamURL;

  // Strip any URL parameters
  //
  if (WorkingURL.indexOf("?") != -1)
  {
    Parts = WorkingURL.split("?");
    WorkingURL = Parts[0];
  }

  // Confirm URL in wwhelp hierarchy
  //
  if (((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/api.htm")) != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/common/html/switch.htm")) != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/common/html/wwhelp.htm")) != -1) ||
      ((Index = WorkingURL.indexOf("/wwhelp/wwhimpl/js/html/wwhelp.htm"))     != -1))
  {
    URLPrefix = WorkingURL.substring(0, Index);
  }
  else
  {
    // Look for match on top level "wwhelp.htm" file
    //
    Index = WorkingURL.lastIndexOf("/");
    if ((Index != -1) &&
       (Index == WorkingURL.indexOf("/wwhelp.htm")))
    {
      URLPrefix = WorkingURL.substring(0, Index);
    }
  }

  return URLPrefix;
}

function  WWHHelp_SetContextDocument(ParamURL)
{
  var  URL = WWHFrame.WWHBrowser.fNormalizeURL(ParamURL);
  var  CurrentURLPrefix;
  var  NewURLPrefix;
  var  VarDocumentFrame;
  var  VarDocumentURL;
  var  VarURLParameters;


  // Confirm URL under same hierarchy
  //
  CurrentURLPrefix = WWHHelp_GetURLPrefix(this.mLocationURL);
  NewURLPrefix     = WWHHelp_GetURLPrefix(URL);
  if ((CurrentURLPrefix != null) &&
      (NewURLPrefix     != null) &&
      (CurrentURLPrefix == NewURLPrefix))
  {
    // Automatically synchornize TOC
    //
    this.mbAutoSyncTOC = true;

    // Check if in single topic mode
    //
    if (this.fSingleTopic())
    {
      // Check for required switch to frameset with navigation
      //
      WWHFrame.WWHSwitch.fProcessURL(ParamURL);
      if (WWHFrame.WWHSwitch.mImplementation != "single")
      {
        // Switch to frameset with navigation
        //
        if (WWHFrame.WWHSwitch.mParameters.length > 0)
        {
          // Context and topic supplied, use them
          //
          this.fSetLocation("WWHFrame", ParamURL);
        }
        else
        {
          // Just switch to frameset with navigation and preserve the current document
          //
          VarDocumentFrame = eval(this.fGetFrameReference("WWHDocumentFrame"));

          VarDocumentURL = WWHFrame.WWHBrowser.fNormalizeURL(VarDocumentFrame.location.href);
          VarDocumentURL = WWHFrame.WWHHelp.fGetBookFileHREF(VarDocumentURL);
          WWHFrame.WWHSwitch.fExec(false, WWHFrame.WWHHelp.mHelpURLPrefix + "/wwhelp/wwhimpl/common/html/wwhelp.htm?href=" + WWHFrame.WWHBrowser.fRestoreEscapedSpaces(VarDocumentURL));
        }
      }
      else
      {
        // Update document frame
        //
        this.fSetDocumentFrameWithURL(URL);
      }
    }
    else
    {
      VarURLParameters = this.fGetURLParameters(URL);

      // Specifies a document to display?
      //
      if ((VarURLParameters[0] != null) ||
          (VarURLParameters[1] != null) ||
          (VarURLParameters[2] != null) ||
          (VarURLParameters[3] != null))
      {
        // Update document frame
        //
        this.fSetDocumentFrameWithURL(URL);

        // Set navigation tab to display
        //
        this.mNewTabName = VarURLParameters[5];
      }
      else
      {
        // Switch tabs
        //
        WWHFrame.WWHHandler.fSetCurrentTab(VarURLParameters[5]);
      }
    }
  }
  else
  {
    // Some other help system requested, redirect to it
    //
    this.fSetLocation("WWHFrame", ParamURL);
  }
}

function  WWHHelp_GetBookFileHREF(ParamHREF)
{
  var  BookFileHREF = null;
  var  Prefix;
  var  Suffix;


  // Confirm HREF can be in same hierarchy as BaseURL
  //
  if ((this.mBaseURL.length > 0) &&
      (ParamHREF.length > this.mBaseURL.length))
  {
    Prefix = ParamHREF.substring(0, this.mBaseURL.length);
    Suffix = ParamHREF.substring(this.mBaseURL.length, ParamHREF.length);

    // Confirm HREF definitely is in same hierarchy as BaseURL
    //
    if (Prefix == this.mBaseURL)
    {
      BookFileHREF = Suffix;
    }
  }

  return BookFileHREF;
}

function  WWHHelp_HREFToBookIndexFileIndexAnchor(ParamHREF)
{
  var  ResultArray = new Array(-1, -1, "");
  var  BookFileHREF;


  BookFileHREF = this.fGetBookFileHREF(ParamHREF);
  if (BookFileHREF != null)
  {
    ResultArray = this.mBooks.fHREFToBookIndexFileIndexAnchor(BookFileHREF);
  }

  return ResultArray;
}

function  WWHHelp_GetSyncPrevNext(ParamHREF)
{
  var  ResultArray = new Array(null, null, null);
  var  Parts;
  var  AbsoluteHREF;
  var  VarAnchor;
  var  BookFileHREF;


  // Trim named anchor entries
  //
  Parts = ParamHREF.split("#");
  AbsoluteHREF = Parts[0];
  VarAnchor = "";
  if (Parts.length > 1)
  {
    if (Parts[1].length > 0)
    {
      VarAnchor = "#" + Parts[1];
    }
  }

  BookFileHREF = this.fGetBookFileHREF(AbsoluteHREF);
  if (BookFileHREF != null)
  {
    if (BookFileHREF == "wwhelp/wwhimpl/common/html/default.htm")
    {
      ResultArray[2] = this.mBooks.fBookFileIndiciesToHREF(0, 0);
    }
    else
    {
      ResultArray = this.mBooks.fGetSyncPrevNext(BookFileHREF);
    }

    // Prefix with BaseURL if defined
    //

    // Current
    //
    if (ResultArray[0] != null)
    {
      ResultArray[0] = this.mBaseURL + ResultArray[0] + VarAnchor;
    }

    // Previous
    //
    if (ResultArray[1] != null)
    {
      ResultArray[1] = this.mBaseURL + ResultArray[1];
    }

    // Next
    //
    if (ResultArray[2] != null)
    {
      ResultArray[2] = this.mBaseURL + ResultArray[2];
    }
  }
  else
  {
    // Unknown document, enable next button to go to first known page
    //
    ResultArray[2] = this.mBaseURL + this.mBooks.fBookFileIndiciesToHREF(0, 0);
  }

  return ResultArray;
}

function  WWHHelp_HREFToTitle(ParamHREF)
{
  var  Title;
  var  Parts;
  var  AbsoluteHREF;


  // Try to find book and file
  //
  Parts = this.fHREFToBookIndexFileIndexAnchor(ParamHREF);
  if ((Parts[0] >= 0) &&
      (Parts[1] >= 0))
  {
    Title = this.mBooks.fBookIndexFileIndexToTitle(Parts[0], Parts[1]);
  }
  else
  {
    // Use basename for title
    //
    Parts = ParamHREF.split("#");
    AbsoluteHREF = Parts[0];
    Parts = AbsoluteHREF.split("/");
    Title = Parts[Parts.length - 1];
  }

  return Title;
}

function  WWHHelp_EscapeHTML(ParamText)
{
  return WWHStringUtilities_EscapeHTML(ParamText);
}

function  WWHHelp_PopupHTML()
{
  var  VarHTML = "";


  if ((WWHFrame.WWHBrowser.mbSupportsPopups) &&
      (WWHFrame.WWHBrowser.mbSupportsIFrames))
  {
    VarHTML = this.mPopup.fDivTagText();
  }

  return VarHTML;
}

function  WWHHelp_ShowPopup(ParamContext,
                            ParamLink,
                            ParamEvent)
{
  var  Book;
  var  Link;
  var  Src;
  var  PopupHTML;

  if ((WWHFrame.WWHBrowser.mbSupportsPopups) &&
      (WWHFrame.WWHBrowser.mbSupportsIFrames))
  {
    Book = this.mBooks.fGetContextBook(ParamContext);
    if (Book != null)
    {
      Link = WWHFrame.WWHBrowser.fNormalizeURL(ParamLink);
      Src = this.mBaseURL + Book.mDirectory + Link;
      PopupHTML = "<iframe id=\"WWHPopupIFrame\" width=\"" + this.mPopup.mWidth + "\" src=\"" + Src + "\" onload=\"javascript:WWHPopupLoaded()\"></iframe>";
      this.mPopup.fShow(PopupHTML, ParamEvent);
    }
  }
}

function  WWHHelp_PopupLoaded()
{
  var  VarPopupDocument;
  var  VarIFrame;
  var  VarHeight;

  if ((WWHFrame.WWHBrowser.mbSupportsPopups) &&
      (WWHFrame.WWHBrowser.mbSupportsIFrames))
  {
    // Access popup document
    //
    VarPopupDocument = eval(this.mPopup.mWindowRef + ".document");

    // Access popup iframe
    //
    if (WWHFrame.WWHBrowser.mBrowser == 2)  // Shorthand for Internet Explorer
    {
      VarIFrame = VarPopupDocument.all['WWHPopupIFrame'];

      // Set height again before width set
      //
      VarHeight = VarIFrame.contentWindow.document.body.scrollHeight + 4;
      VarIFrame.height = VarHeight;

      // Set width
      //
      VarWidth = VarIFrame.contentWindow.document.body.scrollWidth + 4;
      if (VarWidth > this.mPopup.mWidth)
      {
        VarIFrame.width = VarWidth;
      }

      // Set height again after width set
      //
      VarHeight = VarIFrame.contentWindow.document.body.scrollHeight + 4;
      VarIFrame.height = VarHeight;
    }
    else
    {
      VarIFrame = VarPopupDocument.getElementById('WWHPopupIFrame');

      // Set width
      //
      VarWidth = VarIFrame.contentWindow.innerWidth + VarIFrame.contentWindow.scrollMaxX;
      if (VarWidth > this.mPopup.mWidth)
      {
        VarIFrame.width = VarWidth;
      }

      // Set height
      //
      VarHeight = VarIFrame.contentWindow.innerHeight + VarIFrame.contentWindow.scrollMaxY;
      VarIFrame.height = VarHeight;
    }
  }
}

function  WWHHelp_HidePopup()
{
  this.mPopup.fHide();
}

function  WWHHelp_ClickedPopup(ParamContext,
                               ParamLink,
                               ParamPopupLink)
{
  var  VarTargetLink;

  if ((WWHFrame.WWHBrowser.mbSupportsPopups) &&
      (WWHFrame.WWHBrowser.mbSupportsIFrames))
  {
    // Show popup target
    //
    this.fHidePopup();
    VarTargetLink = ParamLink;
  }
  else
  {
    // Show popup, if defined
    //
    VarTargetLink = ParamPopupLink;
    if (VarTargetLink.length == 0)
    {
      VarTargetLink = ParamLink;
    }
  }

  // Display target link
  //
  this.fDisplayFile(ParamContext, VarTargetLink);
}

function  WWHHelp_DisplayFile(ParamContext,
                              ParamLink)
{
  var  Book;
  var  Link;

  // Link defined?
  //
  if (ParamLink.length > 0)
  {
    Book = this.mBooks.fGetContextBook(ParamContext);
    if (Book != null)
    {
      Link = WWHFrame.WWHBrowser.fNormalizeURL(ParamLink);
      WWHFrame.WWHHelp.fSetDocumentHREF(this.mBaseURL + Book.mDirectory + Link, false);
    }
  }
}

function  WWHHelp_DisplayFirst()
{
  VarURL = WWHFrame.WWHHelp.fGetBookIndexFileIndexURL(0, 0, null);
  WWHFrame.WWHHelp.fSetDocumentHREF(VarURL, true);

  // Automatically synchronize TOC
  //
  this.mbAutoSyncTOC = true;
}

function  WWHHelp_ShowTopic(ParamContext,
                            ParamTopic)
{
  var  VarContextBook;


  // Determine book directory
  //
  VarContextBook = this.mBooks.fGetContextBook(ParamContext);
  if (VarContextBook != null)
  {
    // Setup for a topic search
    //
    this.mContextDir = VarContextBook.mDirectory;
    this.mTopicTag   = ParamTopic;

    this.mDocumentURL = "";

    // Load topic data to determine document to display
    //
    this.fSetDocumentHREF(this.mBaseURL + "wwhelp/wwhimpl/common/html/document.htm", false);
  }
}

function  WWHHelp_Update(ParamURL)
{
  var  URL;
  var  Parts;


  if (this.mbInitialized)
  {
    URL = WWHFrame.WWHBrowser.fNormalizeURL(ParamURL);

    if (WWHFrame.WWHHandler.fIsReady())
    {
      Parts = this.fHREFToBookIndexFileIndexAnchor(URL);
      if ((Parts[0] >= 0) &&
          (Parts[1] >= 0))
      {
        WWHFrame.WWHHandler.fUpdate(Parts[0], Parts[1], Parts[2]);
      }

      this.fDocumentBookkeeping(URL);

      // Switch tabs, if necessary
      //
      if (this.mNewTabName != null)
      {
        WWHFrame.WWHHandler.fSetCurrentTab(this.mNewTabName);

        this.mNewTabName = null;
      }
    }
    else
    {
      // Try again in a bit
      //
      setTimeout("WWHFrame.WWHHelp.fUpdate(\"" + WWHStringUtilities_EscapeForJavaScript(ParamURL) + "\");", 100);
    }
  }
  else if (ParamURL.indexOf("wwhelp/wwhimpl/common/html/default.htm") == -1)
  {
    // Try again in a bit
    //
    this.mDocumentLoaded = ParamURL;
    setTimeout("WWHFrame.WWHHelp.fUpdate(\"" + WWHStringUtilities_EscapeForJavaScript(ParamURL) + "\");", 100);
  }
}

function  WWHHelp_SyncTOC(ParamURL,
                          bParamReportError)
{
  var  Parts;

  if (WWHFrame.WWHHandler.fIsReady())
  {
    Parts = this.fHREFToBookIndexFileIndexAnchor(ParamURL);
    if ((Parts[0] >= 0) &&
        (Parts[1] >= 0))
    {
      WWHFrame.WWHHandler.fSyncTOC(Parts[0], Parts[1], Parts[2], bParamReportError);
    }
  }
  else
  {
    // Try again in a bit
    //
    setTimeout("WWHFrame.WWHHelp.fSyncTOC(\"" + WWHStringUtilities_EscapeForJavaScript(ParamURL) + "\", " + bParamReportError + ");", 100);
  }
}

function  WWHHelp_FavoritesCurrent(ParamURL)
{
  var  Parts;

  if (WWHFrame.WWHHandler.fIsReady())
  {
    Parts = this.fHREFToBookIndexFileIndexAnchor(ParamURL);
    if ((Parts[0] >= 0) &&
        (Parts[1] >= 0))
    {
      WWHFrame.WWHHandler.fFavoritesCurrent(Parts[0], Parts[1]);
    }
    else
    {
      WWHFrame.WWHHandler.fFavoritesCurrent(-1, -1);
    }
  }
  else
  {
    // Try again in a bit
    //
    setTimeout("WWHFrame.WWHHelp.fFavoritesCurrent(\"" + WWHStringUtilities_EscapeForJavaScript(ParamURL) + "\");", 100);
  }
}

function  WWHHelp_DocumentBookkeeping(ParamURL)
{
  var  VarDocumentFrame;
  var  VarURL;

  // Highlight search words
  //
  if (typeof(WWHFrame.WWHHighlightWords) != "undefined")
  {
    WWHFrame.WWHHighlightWords.fExec();
  }

  // Update controls
  //
  WWHFrame.WWHControls.fUpdateHREF(ParamURL);

  // Update window title, if possible
  //
  if (ParamURL.indexOf("wwhelp/wwhimpl/common/html/default.htm") == -1)
  {
    if (WWHFrame.WWHBrowser.mBrowser != 1)  // Shorthand for Netscape
    {
      WWHFrame.document.title = WWHStringUtilities_UnescapeHTML(this.fHREFToTitle(ParamURL));
    }
  }

  // Automatically synchronize TOC, if requested
  //
  this.fAutoSyncTOC();

  // Update favorites
  //
  VarDocumentFrame = eval(this.fGetFrameReference("WWHDocumentFrame"));
  VarURL = WWHFrame.WWHBrowser.fNormalizeURL(VarDocumentFrame.location.href);
  this.fFavoritesCurrent(VarURL);
}

function  WWHHelp_AutoSyncTOC()
{
  var  VarDocumentFrame;
  var  VarURL;

  // Automatically synchronize TOC, if requested
  //
  if (this.mbAutoSyncTOC)
  {
    if (WWHFrame.WWHHandler.fGetCurrentTab() == "contents")
    {
      VarDocumentFrame = eval(this.fGetFrameReference("WWHDocumentFrame"));
      VarURL = WWHFrame.WWHBrowser.fNormalizeURL(VarDocumentFrame.location.href);
      this.fSyncTOC(VarURL, false);
    }

    this.mbAutoSyncTOC = this.mbAlwaysSyncTOC;
  }
}

function  WWHHelp_Unload()
{
  // Clear related topics list
  //
  WWHFrame.WWHRelatedTopics.fClear();
}

function  WWHHelp_IgnoreNextKeyPress(ParamEvent)
{
  if (this.mbInitialized)
  {
    if ((ParamEvent != null) &&
        (typeof(ParamEvent.keyCode) != "undefined"))
    {
      this.mbIgnoreNextKeyPress = true;
    }
  }

  return true;
}

function  WWHHelp_HandleKeyDown(ParamEvent)
{
  if (this.mbInitialized)
  {
    if ((ParamEvent != null) &&
        (typeof(ParamEvent.keyCode) != "undefined"))
    {
      if (ParamEvent.keyCode == 18)
      {
        this.mbAltKeyDown = true;
      }
      else if ((ParamEvent.keyCode >= 48) &&
               (ParamEvent.keyCode <= 57))
      {
        this.mAccessKey = ParamEvent.keyCode - 48;
      }
    }
  }

  return true;
}

function  WWHHelp_HandleKeyPress(ParamEvent)
{
  if (this.mbInitialized)
  {
    if (ParamEvent != null)
    {
      if (this.mbIgnoreNextKeyPress)
      {
        // Ignore this key press event
        //
      }
      else
      {
        if (this.mAccessKey != null)
        {
          this.fProcessAccessKey(this.mAccessKey);
        }
      }
    }

    // Reset to handle next access key
    //
    this.mbIgnoreNextKeyPress = false;
    this.mAccessKey = null;
  }

  return true;
}

function  WWHHelp_HandleKeyUp(ParamEvent)
{
  if (this.mbInitialized)
  {
    if ((ParamEvent != null) &&
        (typeof(ParamEvent.keyCode) != "undefined"))
    {
      if (ParamEvent.keyCode == 18)
      {
        this.mbAltKeyDown = false;
      }
    }
  }

  return true;
}

function  WWHHelp_ProcessAccessKey(ParamAccessKey)
{
  switch (ParamAccessKey)
  {
    case 1:
    case 2:
    case 3:
      WWHFrame.WWHHandler.fProcessAccessKey(ParamAccessKey);
      break;

    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
      WWHFrame.WWHControls.fProcessAccessKey(ParamAccessKey);
      break;

    case 0:
      this.fFocus("WWHDocumentFrame");
      break;
  }
}

function  WWHHelp_Focus(ParamFrameName,
                        ParamAnchorName)
{
  WWHFrame.WWHBrowser.fFocus(this.fGetFrameReference(ParamFrameName), ParamAnchorName);
}

function  WWHHelpUtilities_PreloadGraphics()
{
  var  VarImageDirectory = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images";
  var  VarImage = new Image();


  VarImage.src = VarImageDirectory + "/bkmark.gif";
  VarImage.src = VarImageDirectory + "/bkmarkx.gif";
  VarImage.src = VarImageDirectory + "/close.gif";
  VarImage.src = VarImageDirectory + "/doc.gif";
  VarImage.src = VarImageDirectory + "/email.gif";
  VarImage.src = VarImageDirectory + "/emailx.gif";
  VarImage.src = VarImageDirectory + "/fc.gif";
  VarImage.src = VarImageDirectory + "/fo.gif";
  VarImage.src = VarImageDirectory + "/next.gif";
  VarImage.src = VarImageDirectory + "/nextx.gif";
  VarImage.src = VarImageDirectory + "/prev.gif";
  VarImage.src = VarImageDirectory + "/prevx.gif";
  VarImage.src = VarImageDirectory + "/print.gif";
  VarImage.src = VarImageDirectory + "/printx.gif";
  VarImage.src = VarImageDirectory + "/related.gif";
  VarImage.src = VarImageDirectory + "/relatedi.gif";
  VarImage.src = VarImageDirectory + "/relatedx.gif";
  VarImage.src = VarImageDirectory + "/shownav.gif";
  VarImage.src = VarImageDirectory + "/spacer4.gif";
  VarImage.src = VarImageDirectory + "/spc1w2h.gif";
  VarImage.src = VarImageDirectory + "/spc1w7h.gif";
  VarImage.src = VarImageDirectory + "/spc2w1h.gif";
  VarImage.src = VarImageDirectory + "/spc5w1h.gif";
  VarImage.src = VarImageDirectory + "/spc_tb_l.gif";
  VarImage.src = VarImageDirectory + "/spc_tb_m.gif";
  VarImage.src = VarImageDirectory + "/spc_tb_r.gif";
  VarImage.src = VarImageDirectory + "/spc_tb_t.gif";
  VarImage.src = VarImageDirectory + "/sync.gif";
  VarImage.src = VarImageDirectory + "/syncx.gif";
  VarImage.src = VarImageDirectory + "/toolsbg.gif";
}
