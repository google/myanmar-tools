# How to Update Myanmar Tools in Package Repos

This document describes the steps required to update Myanmar Tools in package repositories such as Maven Central and npm.

## Versioning

Version tags apply to the entire repository, but each language has a suffix on the tag.  For example, `v1.1.2+js` is version 1.1.2 for the JavaScript client.

We use semantic versioning: change the small digit for minor bug fixes, the middle digit for larger bug fixes or minor feature improvements, and the large digit for major feature improvements that could also break backwards compatibility.

## Prerequisites

You must have write access to the Github repo, and you must also have write access for this project on the corresponding package repository.

## Releasing to npm (Node.JS)

1. Create an account at https://www.npmjs.com/.
2. On the command line, sign in using `npm login`.
3. Save the desired version to publish in `package.json` and commit.
4. Ensure that the working directory is clean.  Commit or stash any lingering changes.
5. Tag the commit with `v#.#.#+js`
6. Run `npm publish` in the `clients/js` directory.
7. All done; make sure that the package got updated by visiting https://www.npmjs.com/package/myanmar-tools.

## Releasing to Google Hosted Libraries (Browser JS)

A Google engineer needs to perform this task.  For more details, see go/zawgyi/releasing.

## Releasing to Maven Central (Java)

### Requesting Access to Maven Central

1. Create an account at https://issues.sonatype.org/browse/OSSRH.  I recommend generating a random password that you haven't used anywhere else because you will be saving it in plaintext in step 3.
2. If you don't already have a public/private key pair created with gpg, you'll need to get one.
    a. Install the gnupg package if it isn't already installed.
    b. Run `gpg --gen-key`.  I recommend generating a random password that you haven't used anywhere else because you will be saving it in plaintext in step 3.
    c. You'll need to register your new public key with a number of public key servers:
        - http://pgp.mit.edu:11371
        - http://pool.sks-keyservers.net:11371
        - http://keyserver.ubuntu.com:11371
    d. Use gpg --armor --export your-email, where your-email is the email you provided when creating the key. Copy all the text output, including "-----BEGIN PGP PUBLIC KEY BLOCK-----", and paste and submit on each of the above sites.
3. Save all this information in `~/.m2/settings.xml`, which you may need to create.  Boilerplate:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>INSERT_YOUR_SONATYPE_USERNAME_HERE</username>
      <password>INSERT_YOUR_SONATYPE_PASSWORD_HERE</password>
    </server>
  </servers>
  <profiles>
    <profile>
      <id>release</id>
      <activation>
    <property>
      <name>release</name>
    </property>
      </activation>
      <properties>
        <gpg.skip>false</gpg.skip>
        <gpg.passphrase>INSERT_YOUR_GPG_PASSWORD_HERE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

NOTE: If you don't want to save your username and password in plaintext, [this video](https://www.youtube.com/watch?v=b5D2EBjLp40) has instructions on how to generate a token to be used instead.

### Pusing to Maven Central

1. Ensure that `clients/java/pom.xml` has the new version number, with the "-SNAPSHOT" suffix.  If necessary, change the version number and commit.
2. Make sure that your working copy is clean.  Commit or stash any lingering changes.
3. Run `mvn release:prepare` from the `clients/java` directory.  Choose the default for the first and third questions Maven will ask you.  On the second question, use the tag name but add a `+java` to the end of it (for example, `v1.1.0+java`); this is to distringuish tags for releases in different client languages.  This command is a black box that does a lot of work, including making commits to Github with the new version tag.
4. Confirm that there are two new commits on master, both prefixed with "[maven-release-plugin]".  If you don't see these commits, you may need to update the plugin; see https://stackoverflow.com/a/20657721/1407170
5. Once you've confirmed everything is okay, run `mvn release:perform`.

### Update Documentation

The file *clients/java/README.md* has a version hard-coded in two places: the example pom.xml and gradle dependency blocks.  Once the release has been performed, you should update those lines.

## Releasing to RubyGems and PHP Composer/Packagist

There is an account "google-myanmar-tools-user" that has push access to each of these two repositories.  All Googlers on the project can perform pushes using the accounts.  For information on accessing the accounts, see go/valentine.
