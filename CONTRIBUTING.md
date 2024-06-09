<!-- omit in toc -->
# Contributing to CFLib

First off, thanks for taking the time to contribute! ❤️

All types of contributions are encouraged and valued. See the [Table of Contents](#table-of-contents) for different ways to help and details about how this project handles them. Please make sure to read the relevant section before making your contribution. It will make it a lot easier for us maintainers and smooth out the experience for all involved. The community looks forward to your contributions.

> And if you like the project, but just don't have time to contribute, that's fine. There are other easy ways to support the project and show your appreciation, which we would also be very happy about:
> - Star the project
> - Tweet about it
> - Refer this project in your project's readme
> - Mention the project at local meetups and tell your friends/colleagues

<!-- omit in toc -->
## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [I Have a Question](#i-have-a-question)
- [I Want To Contribute](#i-want-to-contribute)
  - [Reporting Bugs](#reporting-bugs)
  - [Suggesting Enhancements](#suggesting-enhancements)
  - [Your First Code Contribution](#your-first-code-contribution)
  - [Improving The Documentation](#improving-the-documentation)
- [Styleguides](#styleguides)
  - [Commit Messages](#commit-messages)
  - [Branches & Pull Requests](#branches--pull-requests)
    - [Branches](#branches)
    - [Pull Requests](#pull-requests)
- [Join The Project Team](#join-the-project-team)


## Code of Conduct

This project and everyone participating in it is governed by the
[CFLib Code of Conduct](https://github.com/AtomicRobotics3805/CFLibblob/master/CODE_OF_CONDUCT.md).
By participating, you are expected to uphold this code. Please report unacceptable behavior
to <cflibftc@gmail.com>.


## I Have a Question

> If you want to ask a question, we assume that you have read the available [Documentation](https://docs.atomicrobotics3805.org).

Before you ask a question, it is best to search for existing [Issues](https://github.com/AtomicRobotics3805/CFLib/issues) that might help you. In case you have found a suitable issue and still need clarification, you can write your question in this issue. It is also advisable to search the internet for answers first.

If you then still feel the need to ask a question and need clarification, we recommend the following:

- Open an [Issue](https://github.com/AtomicRobotics3805/CFLib/issues/new).
- Provide as much context as you can about what you're running into.
- Provide project and platform versions (nodejs, npm, etc), depending on what seems relevant.

We will then take care of the issue as soon as possible.

<!--
You might want to create a separate issue tag for questions and include it in this description. People should then tag their issues accordingly.

Depending on how large the project is, you may want to outsource the questioning, e.g. to Stack Overflow or Gitter. You may add additional contact and information possibilities:
- IRC
- Slack
- Gitter
- Stack Overflow tag
- Blog
- FAQ
- Roadmap
- E-Mail List
- Forum
-->

## I Want To Contribute

> ### Legal Notice <!-- omit in toc -->
> When contributing to this project, you must agree that you have authored 100% of the content, that you have the necessary rights to the content and that the content you contribute may be provided under the project license.

### Reporting Bugs

<!-- omit in toc -->
#### Before Submitting a Bug Report

A good bug report shouldn't leave others needing to chase you up for more information. Therefore, we ask you to investigate carefully, collect information and describe the issue in detail in your report. Please complete the following steps in advance to help us fix any potential bug as fast as possible.

- Make sure that you are using the latest version.
- Determine if your bug is really a bug and not an error on your side e.g. using incompatible environment components/versions (Make sure that you have read the [documentation](https://docs.atomicrobotics3805.org). If you are looking for support, you might want to check [this section](#i-have-a-question)).
- To see if other users have experienced (and potentially already solved) the same issue you are having, check if there is not already a bug report existing for your bug or error in the [bug tracker](https://github.com/AtomicRobotics3805/CFLibissues?q=label%3Abug).
- Also make sure to search the internet (including Stack Overflow) to see if users outside of the GitHub community have discussed the issue.
- Collect information about the bug:
  - Stack trace (Traceback)
  - OS, Platform and Version (Windows, Linux, macOS, x86, ARM)
  - Version of the interpreter, compiler, SDK, runtime environment, package manager, depending on what seems relevant.
  - Possibly your input and the output
  - Can you reliably reproduce the issue? And can you also reproduce it with older versions?

<!-- omit in toc -->
#### How Do I Submit a Good Bug Report?

> You must never report security related issues, vulnerabilities or bugs including sensitive information to the issue tracker, or elsewhere in public. Instead sensitive bugs must be sent by email to `cflibftc@gmail.com`.
<!-- You may add a PGP key to allow the messages to be sent encrypted as well. -->

We use GitHub issues to track bugs and errors. If you run into an issue with the project:

- Open an [Issue](https://github.com/AtomicRobotics3805/CFLib/issues/new). (Since we can't be sure at this point whether it is a bug or not, we ask you not to talk about a bug yet and not to label the issue.)
- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else can follow to recreate the issue on their own. This usually includes your code. For good bug reports you should isolate the problem and create a reduced test case.
- Provide the information you collected in the previous section.

Once it's filed:

- The project team will label the issue accordingly.
- A team member will try to reproduce the issue with your provided steps. If there are no reproduction steps or no obvious way to reproduce the issue, the team will ask you for those steps and mark the issue as `needs-repro`. Bugs with the `needs-repro` tag will not be addressed until they are reproduced.
- If the team is able to reproduce the issue, it will be marked `needs-fix`, as well as possibly other tags (such as `critical`), and the issue will be left to be implemented by someone.

<!-- You might want to create an issue template for bugs and errors that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->


### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for CFLib, **including completely new features and minor improvements to existing functionality**. Following these guidelines will help maintainers and the community to understand your suggestion and find related suggestions.

<!-- omit in toc -->
#### Before Submitting an Enhancement

- Make sure that you are using the latest version.
- Read the [documentation](https://docs.atomicrobotics3805.org) carefully and find out if the functionality is already covered, maybe by an individual configuration.
- Perform a [search](https://github.com/AtomicRobotics3805/CFLib/issues) to see if the enhancement has already been suggested. If it has, add a comment to the existing issue instead of opening a new one.
- Find out whether your idea fits with the scope and aims of the project. It's up to you to make a strong case to convince the project's developers of the merits of this feature. Keep in mind that we want features that will be useful to the majority of our users and not just a small subset. If you're just targeting a minority of users, consider writing an add-on/plugin library.

<!-- omit in toc -->
#### How Do I Submit a Good Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://github.com/AtomicRobotics3805/CFLib/issues).

- Use a **clear and descriptive title** for the issue to identify the suggestion.
- Provide a **step-by-step description of the suggested enhancement** in as many details as possible.
- **Describe the current behavior** and **explain which behavior you expected to see instead** and why. At this point you can also tell which alternatives do not work for you.
- You may want to **include screenshots and animated GIFs** which help you demonstrate the steps or point out the part which the suggestion is related to. You can use [this tool](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and [this tool](https://github.com/colinkeenan/silentcast) or [this tool](https://github.com/GNOME/byzanz) on Linux. <!-- this should only be included if the project has a GUI -->
- **Explain why this enhancement would be useful** to most CFLib users. You may also want to point out the other projects that solved it better and which could serve as inspiration.

<!-- You might want to create an issue template for enhancement suggestions that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->


### Improving The Documentation
Improvements and additions to the documentation are always welcome and encouraged. There is a separate [documentation repository](https://github.com/AtomicRobotics3805/CFLib-Docs) that contains instructions and information about contributing to the [documentation](https://docs.atomicrobotics3805.org). 

## Styleguides
When working with [GitHub](https://github.com) and [Git](https://git-scm.com), a standard system has been established to keep the process running as smoothly as possible. That being said, violations to this style, such as in commit messages, aren't strictly enforced, as long as there is clear explanation of what the contribution accomplishes. If a contribution has no clear goal or benefit, the pull request is likely to be rejected. 

### Commit Messages
Commits should be only a single subject each. As a result, commit messages should only need to explain one new feature, change, patch, or other modification. You can use `git add <FILE>` to add specific files to your commit. If you want to add parts of files to your commit, you can use the `-p` flag: `git add -p <FILE>`. Git will then guide you through the process of selecting which parts of your file you want to include in the commit. 

The title of the commit should give a quick explanation of what the commit is so that someone reading it could understand the general goal the author had when making those changes.

The body of the commit should go into more detail on what specifically was changed, and why. As a general rule, it should answer these 3 questions:
- What was changed?
- What's the reason for the change?
- Is there anything to watch out for or anything particularly remarkable?

### Branches & Pull Requests
Because this project is a public resource that teams are actively using, we must be very careful in what gets pushed into releases. As a result, there is a predetermined system in place for branches and pull requests that must be followed. 

#### Branches
First, the `main` branch is the branch that all releases are created from. Each commit should correspond to a single release. 

The `development` branch is the branch that individual features are merged into. This is the branch that every contributor's pull requests should be targeting, whether they are pull requests from a fork of the repository, or pull requests from a different branch made by the core developers.

Both `main` and `development` are **persistent** branches. They have branch protection rules on, and will never be deleted. 

`feature-xxxxxxxx` branches are branches created by the core developers that are features currently being worked on and/or tested. They will eventually be merged into the `development` branch to be included in subsequent releases. They will be deleted once the pull request is approved.

*Why does this concern you?*

Well this is where you come into play. When you have a contribution you want to make, the first step is to fork the repository. Once you have forked the repository, create a branch that follows the same naming system as above. It should begin with `feature-` then have a word (or two) that explains what the feature is. For example, if someone were working on optimizing parallel command groups, the branch would be called something like `feature-parallel-command-group-optimization`. It's a mouthful, we know, but it will keep our commit history looking clean and tidy at all times. Additionally, developers will be able to see the general feature the feature branch adds without even having to open it. 

#### Pull Requests
Now it's time to talk about pull requests.

A well-done pull request should be clear in what the new feature(s) are and why they are important. Just like commit messages, feel free to give some detail about why exactly you took the time to make these changes and how they impact CFLib and its users. If you give a clear and detailed explanation about your changes, you will greatly improve the likelihood of getting the PR approved. Of course, that's not the only thing that will affect it -- we need to look at the code, too, to make sure it doesn't break anything, but a clear PR is a great start.

When submitting your PR, please *make sure to set the base branch to `development`.* Pull requests submitted to main will not be approved under any circumstances. The only pull requests into `main` are those from a temporary release branch, where additional testing is done and bugs are fixed. These release branches will be created by the core developers, but can be tested by anyone.

To test code on branches other than `main`, follow the following scheme: `com.github.AtomicRobotics3805:CFLib:<branch-name>-SNAPSHOT`. This will give you the latest commit of a specific branch. Refer to [Jitpack](https://jitpack.io) for more details. 

**NOTE FOR DEVELOPMENT TEAM**: When a commit is merged from `development` into `main`, you must **ALWAYS** use the `squash and merge` method. This is because we want to keep the main history as clean and concise as possible, where each commit on main directly corresponds to a new version of CFLib. When merging into `development`, it is preferred if you create a merge commit, but it isn't as important.

## Join The Development Team
If you have been contributing to CFLib for a long time, or have made many important contributions, you may be considered for joining the core development team. You will gain a special role in the [CFLib Discord](https://discord.com/invite/PjP9Ze6fkX) and will gain additional abilities in the GitHub repository such as approving pull requests.

To apply to join the team, send an email to `cflibftc@gmail.com` with an explanation of what your capabilities are, what you've done already to help the library, and your GitHub username (so we can see what your contributions are). After that, we will review it amongst ourselves and decide if you will be added to the team. 

<!-- omit in toc -->
## Attribution
This guide was created in part by using the [contributing.md](https://contributing.md) generator. Check out the repository here: https://github.com/bttger/contributing-gen

The commit message prompts and branch organization system were based off of and inspired by the YouTube video by Tobias Günther via FreeCodeCamp. You can view it [here](https://www.youtube.com/watch?v=Uszj_k0DGsg).