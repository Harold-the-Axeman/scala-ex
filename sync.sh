
# Sync Config files
rsync -azv --delete conf/ ~/conf
rsync -azv --delete conf/ prod1:~/conf
rsync -azv --delete conf/ prod2:~/conf

rsync -azv --delete conf/ cms1:~/conf

rsync -azv --delete conf/ ws1:~/conf

# App
rsync -azv  services/app/target/universal/qidian-app-1.0.zip ~/qidian-app-1.0.zip
rsync -azv  services/app/target/universal/qidian-app-1.0.zip prod1:~/qidian-app-1.0.zip
rsync -azv  services/app/target/universal/qidian-app-1.0.zip prod2:~/qidian-app-1.0.zip
# CMS
rsync -azv  services/cms/target/universal/qidian-cms-1.0.zip cms1:~/qidian-cms-1.0.zip
# WS
rsync -azv  services/ws/target/universal/qidian-ws-1.0.zip ws1:~/qidian-ws-1.0.zip

