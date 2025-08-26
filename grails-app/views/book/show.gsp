<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${book?.title?.encodeAsHTML()}</title>
</head>

<body>
<h1>${book?.title?.encodeAsHTML()}</h1>

<div class="row" style="margin-bottom:1rem;">
    <div class="col-md-3" style="text-align:center;">
        <g:if test="${book?.thumbnailUrl}">
            <img src="${book.thumbnailUrl}" alt="${book.title?.encodeAsHTML()}" style="max-width:100%;"/>
        </g:if>
    </div>

    <div class="col-md-9">
        <p><strong>Authors:</strong> ${book?.authors ?: '-'}</p>

        <p><strong>Categories:</strong> ${book?.categories ?: '-'}</p>

        <p><strong>Published:</strong> ${book?.publishedDateRaw ?: '-'}</p>

        <p><strong>Pages:</strong> ${book?.pageCount ?: '-'}</p>

        <p><strong>Google ID:</strong> ${book?.googleId ?: '-'}</p>

        <p><strong>Description:</strong><br/> ${book?.description ?: '-'}</p>

        <p>
            <g:if test="${book?.previewLink}">
                <a href="${book.previewLink}" target="_blank" class="btn btn-default">Preview on Google</a>
            </g:if>
        </p>
    </div>
</div>

<div>
    <g:link controller="book" action="index" class="btn btn-default">Back</g:link>
</div>

</body>
</html>