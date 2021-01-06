# Client Web

The front-end website.

## Known Issue



++Issue
Warning: Using UNSAFE_componentWillMount in strict mode is not recommended and may indicate bugs in your code.

::Analysis
Component "react-helmet" relies on "react-side-effect" in DocumentTitle.

::Status
Resolved. Upgrade react-helmet to react-helmet-async.

