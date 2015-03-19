FROM <#if image?has_content>${image}</#if>
MAINTAINER \"Luca Gioppo\" <gioppo@csi.it>

# ADDING REPOS
RUN rpm -ivh https://yum.puppetlabs.com/puppetlabs-release-el-6.noarch.rpm

# INSTALL PUPPET AND PREPARE NEEDED FILES
RUN yum install -y puppet tar
ADD puppet/modules/ /etc/puppet/modules/
ADD puppet/manifests/site.pp /etc/puppet/manifests/
#ADD common.yaml /var/lib/hiera/common.yaml

# EXECUTE PUPPET STANDALONE
RUN puppet apply /etc/puppet/manifests/<#if puppetFile?has_content>${puppetFile}</#if> --verbose --detailed-exitcodes || [ $? -eq 2 ]

# OPEN UP PORT
#$exposestr

# START APACHE
ENTRYPOINT <#if entrypoint?has_content>${entrypoint}</#if>
CMD <#if cmd?has_content>${cmd}</#if>