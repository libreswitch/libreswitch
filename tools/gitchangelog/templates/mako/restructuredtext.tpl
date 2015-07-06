% for version in data["versions"]:
% if (version["tag"] == "current_version"):

% for section in version["sections"]:
% for commit in section["commits"]:
<%
subject = "%s [%s]" % (commit["subject"], commit["author"])
entry = indent('\n'.join(textwrap.wrap(subject)),
                       first="- ").strip()
%>${entry}

% if commit["body"]:
${indent(commit["body"])}

% endif
% endfor
% endfor
% endif
% endfor
