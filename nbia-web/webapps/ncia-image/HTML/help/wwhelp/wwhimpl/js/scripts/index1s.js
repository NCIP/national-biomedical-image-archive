// Copyright (c) 2000-2003 Quadralay Corporation.  All rights reserved.
//

// Load book index
//
if (WWHBookData_MaxIndexLevel() > WWHFrame.WWHIndex.mMaxLevel)
{
  WWHFrame.WWHIndex.mMaxLevel = WWHBookData_MaxIndexLevel();
}
WWHFrame.WWHIndex.fInitLoadBookIndex(WWHBookData_AddIndexEntries);
