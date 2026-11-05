# Repository validation

Protected default branch: pull requests, three approvals, no deletion/force pushes and automatic Copilot review on new pushes (excluding drafts).

- Project check: **Build and test Servlet**.
- Secret scan: Gitleaks, redacted output.
- CodeQL: **java-kotlin**, high security alerts and errors.

Checks become required after a successful bootstrap PR run. Existing required checks are preserved. New workflow files require merging this PR to run on subsequent PRs. No paid code-quality service is enabled by this change.


