// Copyright (c) 2000-2003 Quadralay Corporation.  All rights reserved.
//

function  WWHBookGroups_Object()
{
  this.mbShowBooks      = false;
  this.mbExpandAllAtTop = false;
  this.mChildren        = new Array();

  this.fAddGrouping  = WWHBookGroups_AddGrouping;
  this.fAddDirectory = WWHBookGroups_AddDirectory;
}

function  WWHBookGroups_AddGrouping(ParamTitle,
                                    bParamExpand,
                                    ParamIcon,
                                    ParamOpenIcon)
{
  var  bExpandBookGrouping;
  var  BookGrouping;


  // Set mbExpand to true if top entries are to be expanded
  //
  bExpandBookGrouping = false;
  if ((typeof(bParamExpand) != "undefined") &&
      (bParamExpand != null) &&
      (bParamExpand == true))
  {
    bExpandBookGrouping = true;
  }
  else
  {
    if (this.mbExpandAllAtTop)
    {
      bExpandBookGrouping = true;
    }
  }

  BookGrouping = new WWHBookGroups_Group_Object(ParamTitle, bExpandBookGrouping, ParamIcon, ParamOpenIcon);

  // Add to children list
  //
  this.mChildren[this.mChildren.length] = BookGrouping;

  return BookGrouping;
}

function  WWHBookGroups_AddDirectory(ParamDirectory,
                                     bParamShow,
                                     bParamExpand,
                                     ParamIcon,
                                     ParamOpenIcon)
{
  var  bExpandBookDirectory;
  var  BookDirectory;


  // Set mbExpand to true if top entries are to be expanded
  //
  bExpandBookDirectory = false;
  if ((typeof(bParamExpand) != "undefined") &&
      (bParamExpand != null) &&
      (bParamExpand == true))
  {
    bExpandBookDirectory = true;
  }
  else
  {
    if (this.mbExpandAllAtTop)
    {
      bExpandBookDirectory = true;
    }
  }

  BookDirectory = new WWHBookGroups_Directory_Object(ParamDirectory, bParamShow, bExpandBookDirectory, ParamIcon, ParamOpenIcon);

  // Set mbShow to default values if not defined
  //
  if ((typeof(bParamShow) == "undefined") ||
      (bParamShow == null))
  {
    BookDirectory.mbShow = this.mbShowBooks;
  }

  // Add to children list
  //
  this.mChildren[this.mChildren.length] = BookDirectory;
}

function  WWHBookGroups_Group_Object(ParamTitle,
                                     bParamExpand,
                                     ParamIcon,
                                     ParamOpenIcon)
{
  this.mbGrouping = true;
  this.mTitle     = ParamTitle;
  this.mbExpand   = false;
  this.mChildren  = new Array();

  this.fAddGrouping  = WWHBookGroups_Group_AddGrouping;
  this.fAddDirectory = WWHBookGroups_Group_AddDirectory;

  // Set mbExpand if override defined
  //
  if ((typeof(bParamExpand) != "undefined") &&
      (bParamExpand != null))
  {
    if (bParamExpand == true)
    {
      this.mbExpand = true;
    }
  }

  // Set mIcon if defined
  //
  if (typeof(ParamIcon) != "undefined")
  {
    this.mIcon = ParamIcon;
  }

  // Set mOpenIcon if defined
  //
  if (typeof(ParamOpenIcon) != "undefined")
  {
    this.mOpenIcon = ParamOpenIcon;
  }
}

function  WWHBookGroups_Group_AddGrouping(ParamTitle,
                                          bParamExpand,
                                          ParamIcon,
                                          ParamOpenIcon)
{
  var  BookGrouping;


  BookGrouping = new WWHBookGroups_Group_Object(ParamTitle, bParamExpand, ParamIcon, ParamOpenIcon);
  this.mChildren[this.mChildren.length] = BookGrouping;

  return BookGrouping;
}

function  WWHBookGroups_Group_AddDirectory(ParamDirectory,
                                           bParamShow,
                                           bParamExpand,
                                           ParamIcon,
                                           ParamOpenIcon)
{
  var  BookDirectory;


  BookDirectory = new WWHBookGroups_Directory_Object(ParamDirectory, bParamShow, bParamExpand, ParamIcon, ParamOpenIcon);
  this.mChildren[this.mChildren.length] = BookDirectory;
}

function  WWHBookGroups_Directory_Object(ParamDirectory,
                                         bParamShow,
                                         bParamExpand,
                                         ParamIcon,
                                         ParamOpenIcon)
{
  this.mbGrouping = false;
  this.mDirectory = ParamDirectory;
  this.mbShow     = true;
  this.mbExpand   = false;

  // Set mbShow if override defined
  //
  if ((typeof(bParamShow) == "undefined") ||
      (bParamShow == null))
  {
    this.mbShow = WWHFrame.WWHHelp.mBookGroups.mbShowBooks;
  }
  else
  {
    if (bParamShow == false)
    {
      this.mbShow = bParamShow;
    }
  }

  // Set mbExpand if override defined
  //
  if ((typeof(bParamExpand) != "undefined") &&
      (bParamExpand != null))
  {
    if (bParamExpand == true)
    {
      this.mbExpand = bParamExpand;
    }
  }

  // Set mIcon if defined
  //
  if (typeof(ParamIcon) != "undefined")
  {
    this.mIcon = ParamIcon;
  }

  // Set mOpenIcon if defined
  //
  if (typeof(ParamOpenIcon) != "undefined")
  {
    this.mOpenIcon = ParamOpenIcon;
  }

  // Add to book list
  //
  WWHFrame.WWHHelp.mBooks.fInit_AddBookDir(ParamDirectory);
}
