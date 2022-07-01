# objects

## org.sonatype.nexus.repository.view.Response;
```yaml
etag: c849a44121f823c806f604d6568d9e89 (class java.lang.String)
last_modified: 2021-10-22T15:57:10.000Z (class org.joda.time.DateTime)
hashCodesMap: (class java.util.HashMap)
    org.sonatype.nexus.common.hash.HashAlgorithm@7ce01bd8: bcbb583a5a4b4767de8ac020dea3d54011fee961
    org.sonatype.nexus.common.hash.HashAlgorithm@5a3efc26: deaf32dcd9ab821e359cd8330786bcd077604b5c5730c0b096eda46f95c24a2d
    org.sonatype.nexus.common.hash.HashAlgorithm@5b0c0740: c849a44121f823c806f604d6568d9e89
org.sonatype.nexus.repository.storage.Asset: 
  metadata: (AttachedEntityMetadata)
    schema: asset
    document:  
      "#74:0": >-
        bucket:#57:1,format:pypi,last_updated:Fri Jul 01 12:46:21 UTC 2022,attributes:[5],component:#65:0,
        name:packages/pip/21.3.1/pip-21.3.1-py3-none-any.whl,size:1723581,content_type:application/zip,created_by:anonymous,
        created_by_ip:80.78.253.49,blob_ref:default@E1F281C2-0A159DAD-4208CEFD-524F990B-0B4DDE11:739a4ecb-73fc-4422-a7d7-c93c8ad069c6,
        last_downloaded:Fri Jul 01 04:44:03 UTC 2022,blob_created:Wed Jun 29 07:01:34 UTC 2022,blob_updated:Wed Jun 29 07:01:34 UTC 2022 
    name: packages/pip/21.3.1/pip-21.3.1-py3-none-any.whl
org.sonatype.nexus.repository.cache.CacheInfo: 
  lastVerified: 2022-07-01T12:46:21.457Z
  cacheToken: 'null'
```

## org.sonatype.nexus.repository.view.Context;
```yaml
security.authorized: true (class java.lang.Boolean)
nexus.analytics.format_request_rates.marked: true (class java.lang.Boolean)
com.sonatype.nexus.repository.pypi.AssetKind: PACKAGE (class com.sonatype.nexus.repository.pypi.AssetKind)
local.attribute.org.sonatype.nexus.repository.view.handlers.HandlerContributor.extended: true (class java.lang.Boolean)
org.sonatype.nexus.repository.view.matchers.token.TokenMatcher$State: >-
  org.sonatype.nexus.repository.view.matchers.token.TokenMatcher$1@19f23c0b ((class org.sonatype.nexus.repository.view.matchers.token.TokenMatcher$1))
```

## org.sonatype.nexus.repository.Repository

```yaml
proxy: (class java.util.HashMap)
  contentMaxAge: 0.0
  remoteUrl: https://pypi.org
  metadataMaxAge: 0.0
negativeCache: (class java.util.HashMap)
  timeToLive: 0.0
  enabled: false 
storage: (class java.util.HashMap)
  strictContentTypeValidation: false
  blobStoreName: default
httpclient: (class java.util.HashMap)
  blocked: false
  connection: 
    useTrustStore: true
    autoBlock: false
```
