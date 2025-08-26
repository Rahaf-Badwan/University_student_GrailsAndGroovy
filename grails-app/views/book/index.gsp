<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Library</title>
</head>

<body>
<h1>Library</h1>

<!-- Search: submitting will import from Google and then list -->
<g:form controller="book" action="index" method="get" class="form-inline" style="margin-bottom:1rem;">
    <g:textField name="q" value="${q}" placeholder="Search books (title, author, category)" class="form-control"
                 required="required"/>
    <g:field type="number" name="externalMax" value="${externalMax ?: 20}" min="1" max="40" class="form-control"/>
    <g:submitButton name="search" value="Search & Save" class="btn btn-primary"/>
    <g:link controller="book" action="index" class="btn btn-outline-primary">Clear</g:link>
</g:form>

<g:if test="${importInfo?.error}">
    <div class="alert alert-danger">${importInfo.error}</div>
</g:if>
<g:elseif test="${q}">
    <p>
        <strong>Imported for:</strong> <code>${q}</code>
        <g:if test="${importInfo}">
            — Saved: ${importInfo.saved ?: 0}, Updated: ${importInfo.updated ?: 0}, Skipped: ${importInfo.skipped ?: 0}
        </g:if>
    </p>
</g:elseif>

<table class="table table-striped table-bordered">
    <thead>
    <tr>
        <th>Cover</th>
        <th>Title</th>
        <th>Authors</th>
        <th>Categories</th>
        <th>Published</th>
        <th>Preview</th>
    </tr>
    </thead>
    <tbody>
    <g:if test="${bookList}">
        <g:each in="${bookList}" var="b">
            <tr>
                <td style="width:90px;text-align:center;">
                    <g:if test="${b.thumbnailUrl}">
                        <img src="${b.thumbnailUrl}" alt="${b.title?.encodeAsHTML()}"
                             style="max-width:80px; max-height:110px;"/>
                    </g:if>
                </td>
                <td><g:link controller="book" action="show" id="${b.id}">${b.title?.encodeAsHTML()}</g:link></td>
                <td>${b.authors ?: '-'}</td>
                <td>${b.categories ?: '-'}</td>
                <td>${b.publishedDateRaw ?: '-'}</td>
                <td>
                    <g:if test="${b.previewLink}">
                        <a href="${b.previewLink}" target="_blank" class="btn btn-sm btn-default">Preview</a>
                    </g:if>
                </td>
            </tr>
        </g:each>
    </g:if>
    <g:else>
        <tr><td colspan="6" style="text-align:center;">No books found</td></tr>
    </g:else>
    </tbody>
</table>

<div class="pagination">
    <g:paginate total="${bookCount ?: 0}" params="${[q: q, externalMax: externalMax]}"/>
</div>

</body>
</html>