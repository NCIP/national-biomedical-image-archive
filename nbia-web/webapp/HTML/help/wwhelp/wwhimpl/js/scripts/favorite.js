// Copyright (c) 2005-2005 Quadralay Corporation.  All rights reserved.
//

function  WWHFavorites_Object()
{
  this.mbPanelInitialized = false;
  this.mPanelAnchor       = null;
  this.mPanelTabTitle     = WWHFrame.WWHJavaScript.mMessages.mTabsFavoritesLabel;
  this.mPanelTabIndex     = -1;
  this.mPanelFilename     = ((WWHFrame.WWHBrowser.mBrowser == 1) ? "panelfnf.htm" : "panelfsf.htm");
  this.mEventString       = WWHPopup_EventString();
  this.mFavorites         = new Array();
  this.mCurrent           = new WWHFavoritesEntry_Object(-1, -1, "");
  this.mDisplayIndex      = 0;
  this.mHTMLSegment       = new WWHStringBuffer_Object();

  this.fInitHeadHTML           = WWHFavorites_InitHeadHTML;
  this.fInitBodyHTML           = WWHFavorites_InitBodyHTML;
  this.fNavigationHeadHTML     = WWHFavorites_NavigationHeadHTML;
  this.fNavigationBodyHTML     = WWHFavorites_NavigationBodyHTML;
  this.fHeadHTML               = WWHFavorites_HeadHTML;
  this.fStartHTMLSegments      = WWHFavorites_StartHTMLSegments;
  this.fAdvanceHTMLSegment     = WWHFavorites_AdvanceHTMLSegment;
  this.fGetHTMLSegment         = WWHFavorites_GetHTMLSegment;
  this.fEndHTMLSegments        = WWHFavorites_EndHTMLSegments;
  this.fPanelNavigationLoaded  = WWHFavorites_PanelNavigationLoaded;
  this.fPanelViewLoaded        = WWHFavorites_PanelViewLoaded;
  this.fHoverTextTranslate     = WWHFavorites_HoverTextTranslate;
  this.fHoverTextFormat        = WWHFavorites_HoverTextFormat;
  this.fGetPopupAction         = WWHFavorites_GetPopupAction;
  this.fReadCookie             = WWHFavorites_ReadCookie;
  this.fWriteCookie            = WWHFavorites_WriteCookie;
  this.fClearCurrent           = WWHFavorites_ClearCurrent;
  this.fSetCurrent             = WWHFavorites_SetCurrent;
  this.fRecorded               = WWHFavorites_Recorded;
  this.fAdd                    = WWHFavorites_Add;
  this.fRemove                 = WWHFavorites_Remove;
  this.fClickedAdd             = WWHFavorites_ClickedAdd;
  this.fClickedRemove          = WWHFavorites_ClickedRemove;
  this.fClickedEntry           = WWHFavorites_ClickedEntry;
}

function  WWHFavorites_InitHeadHTML()
{
  return "";
}

function  WWHFavorites_InitBodyHTML()
{
  this.mHTMLSegment.fReset();

  // Display initializing message
  //
  this.mHTMLSegment.fAppend("<h2>" + WWHFrame.WWHJavaScript.mMessages.mInitializingMessage + "</h2>\n");

  // Read cookie
  //
  this.fReadCookie();

  // Initialized!
  //
  this.mbPanelInitialized = true;

  return this.mHTMLSegment.fGetBuffer();
}

function  WWHFavorites_NavigationHeadHTML()
{
  var  Settings = WWHFrame.WWHJavaScript.mSettings.mFavorites;

  this.mHTMLSegment.fReset()

  // Generate style section
  //
  this.mHTMLSegment.fAppend("<style type=\"text/css\">\n");
  this.mHTMLSegment.fAppend(" <!--\n");
  this.mHTMLSegment.fAppend("  div\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    margin-top: 1pt;\n");
  this.mHTMLSegment.fAppend("    margin-bottom: 1pt;\n");
  this.mHTMLSegment.fAppend("    " + Settings.mFontStyle + ";\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend(" -->\n");
  this.mHTMLSegment.fAppend("</style>\n");

  return this.mHTMLSegment.fGetBuffer();
}

function  WWHFavorites_NavigationBodyHTML()
{
  var  VarOnSubmitAttribute;
  var  VarButtonLabel;

  this.mHTMLSegment.fReset();

  // Current valid and/or known?
  //
  if ((this.mCurrent.mBookIndex >= 0) &&
      (this.mCurrent.mFileIndex >= 0))
  {
    // Recorded?
    //
    if (this.fRecorded(this.mCurrent.mBookIndex,
                       this.mCurrent.mFileIndex))
    {
      VarOnSubmitAttribute = " onSubmit=\"WWHFrame.WWHFavorites.fClickedRemove();\"";
      VarButtonLabel = WWHFrame.WWHJavaScript.mMessages.mFavoritesRemoveButtonLabel;
    }
    else
    {
      VarOnSubmitAttribute = " onSubmit=\"WWHFrame.WWHFavorites.fClickedAdd();\"";
      VarButtonLabel = WWHFrame.WWHJavaScript.mMessages.mFavoritesAddButtonLabel;
    }

    this.mHTMLSegment.fAppend("<form name=\"WWHFavoritesForm\"" + VarOnSubmitAttribute + ">\n");
    this.mHTMLSegment.fAppend(" <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"4px\">\n");
    this.mHTMLSegment.fAppend("  <tr>\n");
    this.mHTMLSegment.fAppend("   <td width=\"100%\">\n");
    this.mHTMLSegment.fAppend("    <table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\">\n");
    this.mHTMLSegment.fAppend("     <tr>\n");
    this.mHTMLSegment.fAppend("      <td width=\"100%\">\n");
    this.mHTMLSegment.fAppend("<div>");
    this.mHTMLSegment.fAppend((this.mCurrent.mTitle.length > 0) ? this.mCurrent.mTitle : "&#160;");
    this.mHTMLSegment.fAppend("</div>");
    this.mHTMLSegment.fAppend("      </td>\n");
    this.mHTMLSegment.fAppend("     </tr>\n");
    this.mHTMLSegment.fAppend("    </table>\n");
    this.mHTMLSegment.fAppend("   </td>\n");
    this.mHTMLSegment.fAppend("   <td>\n");
    this.mHTMLSegment.fAppend("    <input type=\"submit\" value=\"" + VarButtonLabel + "\" />\n");
    this.mHTMLSegment.fAppend("   </td>\n");
    this.mHTMLSegment.fAppend("  </tr>\n");
    this.mHTMLSegment.fAppend(" </table>\n");
    this.mHTMLSegment.fAppend("</form>\n");
  }

  return this.mHTMLSegment.fGetBuffer();
}

function  WWHFavorites_HeadHTML()
{
  var  Settings = WWHFrame.WWHJavaScript.mSettings.mFavorites;

  this.mHTMLSegment.fReset()

  // Generate style section
  //
  this.mHTMLSegment.fAppend("<style type=\"text/css\">\n");
  this.mHTMLSegment.fAppend(" <!--\n");
  this.mHTMLSegment.fAppend("  a:active\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    text-decoration: none;\n");
  this.mHTMLSegment.fAppend("    background-color: " + Settings.mHighlightColor + ";\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend("  a:hover\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    text-decoration: underline;\n");
  this.mHTMLSegment.fAppend("    color: " + Settings.mEnabledColor + ";\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend("  a\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    text-decoration: none;\n");
  this.mHTMLSegment.fAppend("    color: " + Settings.mEnabledColor + ";\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend("  div\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    margin-top: 1pt;\n");
  this.mHTMLSegment.fAppend("    margin-bottom: 1pt;\n");
  this.mHTMLSegment.fAppend("    " + Settings.mFontStyle + ";\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend(" -->\n");
  this.mHTMLSegment.fAppend("</style>\n");

  // Define accessor functions to reduce file size
  //
  this.mHTMLSegment.fAppend("<script type=\"text/javascript\" language=\"JavaScript1.2\">\n");
  this.mHTMLSegment.fAppend(" <!--\n");
  this.mHTMLSegment.fAppend("  function  fC(ParamEntryID)\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    WWHFrame.WWHFavorites.fClickedEntry(ParamEntryID);\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend("\n");
  this.mHTMLSegment.fAppend("  function  fS(ParamEntryID,\n");
  this.mHTMLSegment.fAppend("               ParamEvent)\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    WWHFrame.WWHJavaScript.mPanels.mPopup.fShow(ParamEntryID, ParamEvent);\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend("\n");
  this.mHTMLSegment.fAppend("  function  fH()\n");
  this.mHTMLSegment.fAppend("  {\n");
  this.mHTMLSegment.fAppend("    WWHFrame.WWHJavaScript.mPanels.mPopup.fHide();\n");
  this.mHTMLSegment.fAppend("  }\n");
  this.mHTMLSegment.fAppend(" // -->\n");
  this.mHTMLSegment.fAppend("</script>\n");

  return this.mHTMLSegment.fGetBuffer();
}

function  WWHFavorites_StartHTMLSegments()
{
  this.mDisplayIndex = 0;

  return "";
}

function  WWHFavorites_AdvanceHTMLSegment()
{
  var  VarAccessible = WWHFrame.WWHHelp.mbAccessible;
  var  VarSettings = WWHFrame.WWHJavaScript.mSettings.mFavorites;
  var  VarAccessibilityTitle;
  var  VarMaxIndex;
  var  VarFavoritesEntry;

  this.mHTMLSegment.fReset();

  // Display favorites
  //
  VarAccessibilityTitle = "";
  while (this.mDisplayIndex < this.mFavorites.length)
  {
    VarFavoritesEntry = this.mFavorites[this.mDisplayIndex];

    if (VarAccessible)
    {
      VarAccessibilityTitle = " title=\"" + WWHStringUtilities_EscapeHTML(VarFavoritesEntry.mTitle) + "\"";
    }

    this.mHTMLSegment.fAppend("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"2\">");
    this.mHTMLSegment.fAppend("<tr>");
    this.mHTMLSegment.fAppend("<td width=\"17\" valign=\"middle\">");
    this.mHTMLSegment.fAppend("<a href=\"javascript:fC(" + this.mDisplayIndex + ");\">");
    this.mHTMLSegment.fAppend("<img border=\"0\" src=\"" + WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images/doc.gif\" width=\"17\" height=\"17\">");
    this.mHTMLSegment.fAppend("</a>");
    this.mHTMLSegment.fAppend("</td>");
    this.mHTMLSegment.fAppend("<td width=\"100%\" align=\"left\" valign=\"middle\">");
    this.mHTMLSegment.fAppend("<div>");
    this.mHTMLSegment.fAppend("<a href=\"javascript:fC(" + this.mDisplayIndex + ");\"" + this.fGetPopupAction(this.mDisplayIndex) + VarAccessibilityTitle + ">");
    this.mHTMLSegment.fAppend(VarFavoritesEntry.mTitle);
    this.mHTMLSegment.fAppend("</a>");
    this.mHTMLSegment.fAppend("</div>");
    this.mHTMLSegment.fAppend("</td>");
    this.mHTMLSegment.fAppend("</tr>");
    this.mHTMLSegment.fAppend("</table>\n");

    this.mDisplayIndex++;
  }

  return (this.mHTMLSegment.fSize() > 0);
}

function  WWHFavorites_GetHTMLSegment()
{
  return this.mHTMLSegment.fGetBuffer();
}

function  WWHFavorites_EndHTMLSegments()
{
  return "";
}

function  WWHFavorites_PanelNavigationLoaded()
{
  // Set focus
  //
  WWHFrame.WWHHelp.fFocus("WWHPanelNavigationFrame");
}

function  WWHFavorites_PanelViewLoaded()
{
}

function  WWHFavorites_HoverTextTranslate(ParamEntryID)
{
  var  RetResult = "";
  var  VarFavoritesEntry;

  // Locate specified entry
  //
  if ((ParamEntryID >= 0) &&
      (ParamEntryID < this.mFavorites.length))
  {
    VarFavoritesEntry = this.mFavorites[ParamEntryID];
    RetResult = VarFavoritesEntry.mTitle;
  }

  return RetResult;
}

function  WWHFavorites_HoverTextFormat(ParamWidth,
                                       ParamTextID,
                                       ParamText)
{
  var  FormattedText   = "";
  var  ForegroundColor = WWHFrame.WWHJavaScript.mSettings.mHoverText.mForegroundColor;
  var  BackgroundColor = WWHFrame.WWHJavaScript.mSettings.mHoverText.mBackgroundColor;
  var  BorderColor     = WWHFrame.WWHJavaScript.mSettings.mHoverText.mBorderColor;
  var  ImageDir        = WWHFrame.WWHHelp.mHelpURLPrefix + "wwhelp/wwhimpl/common/images";
  var  ReqSpacer1w2h   = "<img src=\"" + ImageDir + "/spc1w2h.gif\" width=1 height=2>";
  var  ReqSpacer2w1h   = "<img src=\"" + ImageDir + "/spc2w1h.gif\" width=2 height=1>";
  var  ReqSpacer1w7h   = "<img src=\"" + ImageDir + "/spc1w7h.gif\" width=1 height=7>";
  var  ReqSpacer5w1h   = "<img src=\"" + ImageDir + "/spc5w1h.gif\" width=5 height=1>";
  var  Spacer1w2h      = ReqSpacer1w2h;
  var  Spacer2w1h      = ReqSpacer2w1h;
  var  Spacer1w7h      = ReqSpacer1w7h;
  var  Spacer5w1h      = ReqSpacer5w1h;
  var  StyleAtttribute;


  // Set style attribute to insure small image height
  //
  StyleAttribute = " style=\"font-size: 1px; line-height: 1px;\"";

  FormattedText += "<table width=\"" + ParamWidth + "\" border=0 cellspacing=0 cellpadding=0 bgcolor=\"" + BackgroundColor + "\">";
  FormattedText += " <tr>";
  FormattedText += "  <td" + StyleAttribute + " height=2 colspan=5 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td" + StyleAttribute + " height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td" + StyleAttribute + " height=7 colspan=3>" + Spacer1w7h + "</td>";
  FormattedText += "  <td" + StyleAttribute + " height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += "  <td>" + ReqSpacer5w1h + "</td>";
  FormattedText += "  <td width=\"100%\" id=\"" + ParamTextID + "\" style=\"color: " + ForegroundColor + " ; " + WWHFrame.WWHJavaScript.mSettings.mHoverText.mFontStyle + "\">" + ParamText + "</td>";
  FormattedText += "  <td>" + ReqSpacer5w1h + "</td>";
  FormattedText += "  <td bgcolor=\"" + BorderColor + "\">" + ReqSpacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td" + StyleAttribute + " height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += "  <td" + StyleAttribute + " height=7 colspan=3>" + Spacer1w7h + "</td>";
  FormattedText += "  <td" + StyleAttribute + " height=7 bgcolor=\"" + BorderColor + "\">" + Spacer2w1h + "</td>";
  FormattedText += " </tr>";

  FormattedText += " <tr>";
  FormattedText += "  <td" + StyleAttribute + " height=2 colspan=5 bgcolor=\"" + BorderColor + "\">" + Spacer1w2h + "</td>";
  FormattedText += " </tr>";
  FormattedText += "</table>";

  return FormattedText;
}

function  WWHFavorites_GetPopupAction(ParamEntryID)
{
  var  PopupAction = "";


  if (WWHFrame.WWHJavaScript.mSettings.mHoverText.mbEnabled)
  {
    PopupAction += " onMouseOver=\"fS(" + ParamEntryID + ", " + this.mEventString + ");\"";
    PopupAction += " onMouseOut=\"fH();\"";
  }

  return PopupAction;
}

function  WWHFavorites_ReadCookie()
{
  var  VarFavoritesCookie;
  var  VarBooks = WWHFrame.WWHHelp.mBooks;
  var  VarBookContextsAndFavorites;
  var  VarBookContexts;
  var  VarCookieIndexToCurrentIndex;
  var  VarMaxIndex;
  var  VarIndex;
  var  VarCurrentIndex;
  var  VarFavoritesCookieEntries;
  var  VarFavoritesCookieEntry;
  var  VarBookIndex;
  var  VarFileIndex;
  var  VarTitle;
  var  VarFavoritesEntry;

  // Reset favorites
  //
  this.mFavorites.length = 0;

  // Get cookie
  //
  VarFavoritesCookie = WWHFrame.WWHBrowser.fGetCookie(WWHFrame.WWHHelp.mFavoritesCookie);
  if (VarFavoritesCookie != null)
  {
    // Extract book contexts and favorites
    //
    VarBookContextsAndFavorites = VarFavoritesCookie.split("\n");
    if (VarBookContextsAndFavorites.length == 2)
    {
      // Map the current context/book index values to those
      // recorded when the cookie was written.
      //
      VarCookieIndexToCurrentIndex = new Object();
      VarBookContexts = VarBookContextsAndFavorites[0].split(",");
      for (VarMaxIndex = VarBookContexts.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
      {
        VarCurrentIndex = VarBooks.fGetContextIndex(VarBookContexts[VarIndex]);
        if (VarCurrentIndex >= 0)
        {
          VarCookieIndexToCurrentIndex[VarIndex] = VarCurrentIndex;
        }
      }

      // Extract favorites cookie entries
      //
      VarFavoritesCookieEntries = VarBookContextsAndFavorites[1].split(",");
      for (VarMaxIndex = VarFavoritesCookieEntries.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
      {
        VarFavoritesCookieEntry = VarFavoritesCookieEntries[VarIndex].split(":");
        VarBookIndex = parseInt(VarFavoritesCookieEntry[0]);
        VarFileIndex = parseInt(VarFavoritesCookieEntry[1]);

        // Map book index
        //
        if (typeof(VarCookieIndexToCurrentIndex[VarBookIndex]) == "number")
        {
          // Context valid
          //
          VarBookIndex = VarCookieIndexToCurrentIndex[VarBookIndex];
          if (VarFileIndex < VarBooks.fGetBook(VarBookIndex).mFiles.fEntries())
          {
            // File index valid
            // Add favorite
            //
            VarFavoritesEntry = new WWHFavoritesEntry_Object(VarBookIndex,
                                                             VarFileIndex,
                                                             VarBooks.fBookIndexFileIndexToTitle(VarBookIndex, VarFileIndex));
            this.mFavorites[this.mFavorites.length] = VarFavoritesEntry;
          }
        }
      }
    }
  }
}

function  WWHFavorites_WriteCookie()
{
  var  VarBookList = WWHFrame.WWHHelp.mBooks.mBookList;
  var  VarBookContextsAsString;
  var  VarFavoritesAsString;
  var  VarMaxIndex;
  var  VarIndex;
  var  VarFavoritesEntry;
  var  VarFavoritesAsCookie;

  // Record book contexts as string
  //
  VarBookContextsAsString = "";
  for (VarMaxIndex = VarBookList.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
  {
    if (VarBookContextsAsString.length > 0)
    {
      VarBookContextsAsString += ",";
    }

    VarBookContextsAsString += VarBookList[VarIndex].mContext;
  }

  // Record favorites as string
  //
  VarFavoritesAsString = "";
  for (VarMaxIndex = this.mFavorites.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
  {
    VarFavoritesEntry = this.mFavorites[VarIndex];

    if (VarFavoritesAsString.length > 0)
    {
      VarFavoritesAsString += ",";
    }

    VarFavoritesAsString += VarFavoritesEntry.mBookIndex + ":" + VarFavoritesEntry.mFileIndex;
  }

  // Set cookie
  //
  VarFavoritesAsCookie = VarBookContextsAsString + "\n" + VarFavoritesAsString;
  WWHFrame.WWHBrowser.fSetCookie(WWHFrame.WWHHelp.mFavoritesCookie,
                                 VarFavoritesAsCookie,
                                 WWHFrame.WWHHelp.mSettings.mCookiesDaysToExpire);
}

function  WWHFavorites_ClearCurrent()
{
  this.mCurrent.mBookIndex = -1;
  this.mCurrent.mFileIndex = -1;
  this.mCurrent.mTitle = "";
}

function  WWHFavorites_SetCurrent(ParamBookIndex,
                                  ParamFileIndex)
{
  var  VarBooks = WWHFrame.WWHHelp.mBooks;

  if ((ParamBookIndex >= 0) &&
      (ParamFileIndex >= 0) &&
      (ParamBookIndex < VarBooks.mBookList.length) &&
      (ParamFileIndex < VarBooks.fGetBook(ParamBookIndex).mFiles.fEntries()))
  {
    this.mCurrent.mBookIndex = ParamBookIndex;
    this.mCurrent.mFileIndex = ParamFileIndex;
    this.mCurrent.mTitle = VarBooks.fBookIndexFileIndexToTitle(ParamBookIndex, ParamFileIndex);
  }
  else
  {
    this.fClearCurrent();
  }
}

function  WWHFavorites_Recorded(ParamBookIndex,
                                ParamFileIndex)
{
  var  VarRecorded = false;
  var  VarMaxIndex;
  var  VarIndex;
  var  VarFavoritesEntry;

  for (VarMaxIndex = this.mFavorites.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
  {
    VarFavoritesEntry = this.mFavorites[VarIndex];

    if ((VarFavoritesEntry.mBookIndex == ParamBookIndex) &&
        (VarFavoritesEntry.mFileIndex == ParamFileIndex))
    {
      VarRecorded = true;
      break;
    }
  }

  return VarRecorded;
}

function  WWHFavorites_Add(ParamBookIndex,
                           ParamFileIndex)
{
  var  VarBooks = WWHFrame.WWHHelp.mBooks;

  // Current is valid?
  //
  if ((ParamBookIndex >= 0) &&
      (ParamFileIndex >= 0))
  {
    // Recorded?
    //
    if ( ! this.fRecorded(ParamBookIndex,
                          ParamFileIndex))
    {
      if ((ParamBookIndex >= 0) &&
          (ParamFileIndex >= 0) &&
          (ParamBookIndex < VarBooks.mBookList.length) &&
          (ParamFileIndex < VarBooks.fGetBook(ParamBookIndex).mFiles.fEntries()))
      {
        this.mFavorites[this.mFavorites.length] = new WWHFavoritesEntry_Object(ParamBookIndex,
                                                                               ParamFileIndex,
                                                                               VarBooks.fBookIndexFileIndexToTitle(ParamBookIndex, ParamFileIndex));
      }
    }
  }
}

function  WWHFavorites_Remove(ParamBookIndex,
                              ParamFileIndex)
{
  var  VarOffset;
  var  VarMaxIndex;
  var  VarIndex;
  var  VarFavoritesEntry;

  // Current is valid?
  //
  if ((ParamBookIndex >= 0) &&
      (ParamFileIndex >= 0))
  {
    // Resize array minus specified favorite
    //
    VarOffset = 0;
    for (VarMaxIndex = this.mFavorites.length, VarIndex = 0 ; VarIndex < VarMaxIndex ; VarIndex++)
    {
      VarFavoritesEntry = this.mFavorites[VarIndex];

      if ((VarFavoritesEntry.mBookIndex == ParamBookIndex) &&
          (VarFavoritesEntry.mFileIndex == ParamFileIndex))
      {
        VarOffset += 1;
      }
      else
      {
        this.mFavorites[VarIndex - VarOffset] = this.mFavorites[VarIndex];
      }
    }

    this.mFavorites.length = this.mFavorites.length - VarOffset;
  }
}

function  WWHFavorites_ClickedAdd()
{
  // Remove favorite
  //
  this.fAdd(this.mCurrent.mBookIndex,
            this.mCurrent.mFileIndex);

  // Update cookie
  //
  this.fWriteCookie();
}

function  WWHFavorites_ClickedRemove()
{
  // Remove favorite
  //
  this.fRemove(this.mCurrent.mBookIndex,
               this.mCurrent.mFileIndex);

  // Update cookie
  //
  this.fWriteCookie();
}

function  WWHFavorites_ClickedEntry(ParamEntryID)
{
  var  VarFavoritesEntry;
  var  VarURL;


  // Close down any popups we had going to prevent JavaScript errors
  //
  WWHFrame.WWHJavaScript.mPanels.mPopup.fHide();

  // Access favorites entry
  //
  VarFavoritesEntry = this.mFavorites[ParamEntryID];

  // Display favorite
  //
  VarURL = WWHFrame.WWHHelp.fGetBookIndexFileIndexURL(VarFavoritesEntry.mBookIndex,
                                                      VarFavoritesEntry.mFileIndex);
  WWHFrame.WWHHelp.fSetDocumentHREF(VarURL, false);
}

function WWHFavoritesEntry_Object(ParamBookIndex,
                                  ParamFileIndex,
                                  ParamTitle)
{
  this.mBookIndex = ParamBookIndex;
  this.mFileIndex = ParamFileIndex;
  this.mTitle     = ParamTitle;
}
