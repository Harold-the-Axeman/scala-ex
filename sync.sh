
# Sync Config files
rsync -azv --delete singularity-api/conf/ conf
rsync -azv --delete singularity-api/conf/ prod1:~/conf
rsync -azv --delete singularity-api/conf/ prod2:~/conf

# App
rsync -azv --delete singularity-api/services/app/target/universal/qidian-app-1.0.zip prod1:~/qidian-app-1.0.zip
rsync -azv --delete singularity-api/services/app/target/universal/qidian-app-1.0.zip prod2:~/qidian-app-1.0.zip
# CMS
rsync -azv --delete singularity-api/services/cms/target/universal/qidian-cms-1.0.zip qidian-cms-1.0.zip
# WS
rsync -azv --delete singularity-api/services/ws/target/universal/qidian-ws-1.0.zip qidian-ws-1.0.zip

