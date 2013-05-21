// Copyright (c) 2000-2003 Quadralay Corporation.  All rights reserved.
//

function  WWHFile_Object(ParamTitle,
                         ParamHREF)
{
  this.mTitle = ParamTitle;
  this.mHREF  = ParamHREF;
}

function  WWHFileList_Object()
{
  this.mFileList = new Array();
  this.mFileHash = new WWHFileHash_Object();

  this.fEntries          = WWHFileList_Entries;
  this.fAddFile          = WWHFileList_AddFile;
  this.fA                = WWHFileList_AddFile;
  this.fHREFToIndex      = WWHFileList_HREFToIndex;
  this.fHREFToTitle      = WWHFileList_HREFToTitle;
  this.fFileIndexToHREF  = WWHFileList_FileIndexToHREF;
  this.fFileIndexToTitle = WWHFileList_FileIndexToTitle;
}

function  WWHFileList_Entries()
{
  return this.mFileList.length;
}

function  WWHFileList_AddFile(ParamTitle,
                              ParamHREF)
{
  this.mFileHash[unescape(ParamHREF) + "~"] = this.mFileList.length;
  this.mFileList[this.mFileList.length] = new WWHFile_Object(ParamTitle, ParamHREF);
}

function  WWHFileList_HREFToIndex(ParamHREF)
{
  var  MatchIndex = -1;
  var  Match;


  Match = this.mFileHash[ParamHREF + "~"];
  if (typeof(Match) != "undefined")
  {
    MatchIndex = Match;
  }

  return MatchIndex;
}

function  WWHFileList_HREFToTitle(ParamHREF)
{
  var  Title = "";
  var  MatchIndex;


  MatchIndex = this.fHREFToIndex(ParamHREF);
  if (MatchIndex != -1)
  {
    Title = this.mFileList[MatchIndex].mTitle;
  }
  else
  {
    Title = WWHStringUtilities_EscapeHTML(ParamHREF);
  }

  return Title;
}

function  WWHFileList_FileIndexToHREF(ParamIndex)
{
  return this.mFileList[ParamIndex].mHREF;
}

function  WWHFileList_FileIndexToTitle(ParamIndex)
{
  return this.mFileList[ParamIndex].mTitle;
}

function  WWHFileHash_Object()
{
}
