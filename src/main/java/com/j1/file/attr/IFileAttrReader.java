package com.j1.file.attr;

import org.csource.common.NameValuePair;

public abstract interface IFileAttrReader
{
  public abstract NameValuePair[] readFileAttr(String paramString);

  public abstract boolean canHandle(String paramString);
}
